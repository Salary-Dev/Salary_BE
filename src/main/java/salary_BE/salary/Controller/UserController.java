package salary_BE.salary.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.JoinDto;
import salary_BE.salary.Service.UserService;

@RestController
@ResponseBody
@AllArgsConstructor
public class UserController {

    private final UserService userService;

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

}
