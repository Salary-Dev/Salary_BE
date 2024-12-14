package salary_BE.salary.Controller;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.EconomyLetterService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EconomyLetterController {

    private final UserRepository userRepository;
    private final EconomyLetterService economyLetterService;

    // 구독 중인 경제레터 조회
    @GetMapping("economy-letter/subscribe")
    public ResponseEntity<List<Map<String, String>>> subscribeLetter(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUsername();
        User user = userRepository.findByLoginId(loginId);

        List<Map<String, String>> response = economyLetterService.subscribeLetter(user.getId());
        return ResponseEntity.ok(response);
    }
}
