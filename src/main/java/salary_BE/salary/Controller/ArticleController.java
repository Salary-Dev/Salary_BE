package salary_BE.salary.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Service.ArticleService;

@RestController
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/fetch-finance-news")
    public String fetchFinanceNews() {
        // "금융"을 query 파라미터로 전달하여 금융 관련 뉴스를 가져옴
        articleService.fetchAndSaveNewsArticles("금융");
        return "금융 뉴스 기사가 성공적으로 저장되었습니다.";
    }
}
