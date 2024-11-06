package salary_BE.salary.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Service.TrendQuizService;

@RestController
public class TrendQuizController {

    private final TrendQuizService trendQuizService;

    @Autowired
    public TrendQuizController(TrendQuizService trendQuizService) {
        this.trendQuizService = trendQuizService;
    }

    @GetMapping("/trend-quiz")
    public String getTrendQuiz() {
        // 반환 JSON 응답 그대로 반환
        return trendQuizService.getTrendQuiz();
    }
}
