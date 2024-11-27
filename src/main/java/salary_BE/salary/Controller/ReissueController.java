package salary_BE.salary.Controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Domain.Refresh;
import salary_BE.salary.JWT.JWTUtil;
import salary_BE.salary.JWT.LoginFilter;
import salary_BE.salary.Repository.RefreshRepository;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {

            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }
        if(refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try{
            jwtUtil.isExpired(refresh);
        }catch(ExpiredJwtException e){

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        //토큰이 refresh인지 확인(발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){

            //response status code
            return new ResponseEntity<>("invalid category", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            //responsebody
            return new ResponseEntity<>("refresh token not exist", HttpStatus.BAD_REQUEST);
        }

        String loginId = jwtUtil.getLoginId(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", loginId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", loginId, role, 86400000L);

        //Refresh토큰 저장 DB에 기존의 refresh 토큰 삭제 후 새 refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefresh(loginId, newRefresh, 864000000L);
        //response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        return new ResponseEntity<>(HttpStatus.OK);

    }

    private void addRefresh(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = new Refresh();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }
}
