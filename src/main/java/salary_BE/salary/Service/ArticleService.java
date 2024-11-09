package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import salary_BE.salary.Domain.Article;
import salary_BE.salary.Domain.Shorts;
import salary_BE.salary.Repository.ArticleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    public void fetchAndSaveNewsArticles(String query) {
        String url = "https://openapi.naver.com/v1/search/news.json?query=" + query + "&display=20";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray items = jsonResponse.getJSONArray("items");

            List<Article> articles = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                Article article = new Article();
                article.setUrl(item.getString("link"));
                article.setTitle(item.getString("title").replaceAll("<.*?>", "")); // HTML 태그 제거

                // 날짜 파싱 및 설정
                String pubDate = item.optString("pubDate");
                if (!pubDate.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");
                    LocalDateTime dateTime = LocalDateTime.parse(pubDate, formatter);
                    article.setDate(dateTime);
                }

                article.setSource(item.optString("originallink")); // 출처 (없으면 NULL)

                // 조회수 기본값 설정
                article.setHits(0L); // 조회수는 기본값으로 0을 설정

                // Shorts 객체 설정
                // 이 부분은 예시입니다. 실제 Shorts 객체를 어떻게 설정할지에 따라 수정이 필요합니다.
                Shorts shorts = new Shorts();
                article.setShorts(shorts);

                articles.add(article);
            }

            articleRepository.saveAll(articles);
        }
    }
}
