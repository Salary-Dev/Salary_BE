package salary_BE.salary.Controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.TrendQuizService;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class TrendQuizController {

    private final TrendQuizService trendQuizService;
    private final UserRepository userRepository;

    // 트렌드 퀴즈 요청하기
    @GetMapping("/trend-quiz")
    public String getTrendQuiz() {
        // 반환 JSON 응답 그대로 반환
        return trendQuizService.getTrendQuiz();
    }

    // 트렌드 퀴즈 학습 여부
    @PostMapping("/trend-quiz/update-status")
    public ResponseEntity<Map<String, String>> updateTrendQuiz(@RequestParam boolean trend, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        trendQuizService.completeTrendQuiz(trend, user);

        // 상태 업데이트 성공 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}

