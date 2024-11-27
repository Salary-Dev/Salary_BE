package salary_BE.salary.Controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.ArticleDto;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Service.ArticleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;

    @GetMapping("/fetch-finance-news")
    public String fetchFinanceNews() {
        // "금융"을 query 파라미터로 전달하여 금융 관련 뉴스를 가져옴
        articleService.fetchAndSaveNewsArticles("금융뉴스",20);
        return "금융 뉴스 기사가 성공적으로 저장되었습니다.";
    }

    @GetMapping("/word-article-mapping")
    public String fetchWordArticleMapping() {
        List<Word> words = wordRepository.findAll();
        for(Word word : words) {
            String query = word.getWord();
            System.out.println("해당 단어로 검색합니다. 단어 이름 : "+query);
            articleService.fetchAndSaveNewsArticles(query,2);
        }
        return "단어별 2개의 기사를 성공적으로 불러왔습니다.";
    }

    // 아티클 기능 구현
    @GetMapping("/shorts")
    public ResponseEntity<List<ArticleDto>> getShorts() {
        List<ArticleDto> shorts = articleService.getRandomShorts();
        return ResponseEntity.ok(shorts);
    }

    // 아티클 학습 여부
    @PostMapping("shorts/update-status")
    public ResponseEntity<Map<String, String>> updateArticle(@RequestParam boolean article, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        articleService.completeArticle(article, user);

        // 상태 업데이트 성공 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

}
