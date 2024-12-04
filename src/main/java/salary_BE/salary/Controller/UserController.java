package salary_BE.salary.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.DTO.JoinDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.JWT.JWTUtil;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.UserService;
import salary_BE.salary.SocialLogin.GoogleTokenVerifier;

import java.util.Map;

@RestController
@ResponseBody
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/join")
    public String join(@RequestBody JoinDto joinDTO) {
        // 요청 데이터 로그
        System.out.println("Request received at /join: " + joinDTO);

        try {
            userService.joinProcess(joinDTO);
            return "회원가입에 성공하셨습니다!";
        } catch (Exception e) {
            // 에러 로그 출력
            System.err.println("Error during join process: " + e.getMessage());
            return "회원가입 중 에러가 발생했습니다: " + e.getMessage();
        }
    }

    @GetMapping("/existId")
    public String existId(@RequestParam("loginId") String loginId) {

        Boolean isExist = userRepository.existsByLoginId(loginId);
        if (isExist) {
            return "이미 존재하는 아이디입니다.";
        }
        else{
            return "성공";
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        try {
            String idToken = request.get("idToken");

            // 구글 ID 토큰 검증
            GoogleIdToken.Payload payload = GoogleTokenVerifier.verifyToken(idToken);

            // 구글 사용자 정보 추출
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            boolean isNewUser = false;

            // DB에서 사용자 확인
            User user = userRepository.findByLoginId(email);
            if (user == null) {
                // 사용자 없으면 회원가입
                user = new User();
                user.setLoginId(email);
                user.setUsername(name);
                user.setRole("ROLE_USER");
                userRepository.save(user);
                isNewUser = true;
            }

            // JWT 발급
            String token = jwtUtil.createJwt(user.getLoginId(), user.getRole(), 3600000L);



            // 응답에 "isNewUser" 필드 추가
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "isNewUser", isNewUser // 새 유저인지 클라이언트에 전달
            ));
        } catch (Exception e) {
            // 모든 예외 처리
            return ResponseEntity.badRequest().body("Google login failed: " + e.getMessage());
        }
    }

    @PostMapping("/google-join")
    public ResponseEntity<?> saveUserInfo(@RequestBody Map<String, Object> request,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            // JWT에서 유저 정보 가져오기
            Long userId = userDetails.getUser().getId();

            // 요청 데이터에서 닉네임, 나이, 성별 추출
            String nickname = (String) request.get("nickname");
            Integer age = (Integer) request.get("age");
            String gender = (String) request.get("gender");

            // DB 업데이트
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.setNickname(nickname);
            user.setAge(age);
            user.setGender(gender);
            userRepository.save(user);

            return ResponseEntity.ok("유저 정보 업로드");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user information: " + e.getMessage());
        }
    }

    // 닉네임 조회
    @GetMapping("/auth/nickname")
    public ResponseEntity<String> getNickname(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUsername();
        User user = userRepository.findByLoginId(loginId);

        String nickname = user.getNickname();

        return ResponseEntity.ok(nickname);
    }
}