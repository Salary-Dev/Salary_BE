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
import org.json.JSONException;
import salary_BE.salary.Domain.Article;
import salary_BE.salary.Domain.Shorts;
import salary_BE.salary.Repository.ArticleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        // RestTemplate 생성 및 요청 헤더 설정
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 상태 확인
            if (response.getStatusCode() == HttpStatus.OK) {
                // JSON 파싱 및 데이터 처리
                try {
                    JSONObject jsonResponse = new JSONObject(response.getBody());
                    JSONArray items = jsonResponse.getJSONArray("items");

                    List<Article> articles = new ArrayList<>();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Article article = new Article();

                        // 링크와 제목 설정
                        article.setUrl(item.getString("link"));
                        article.setTitle(item.getString("title").replaceAll("<.*?>", "")); // HTML 태그 제거

                        // 날짜 파싱 및 설정
                        String pubDate = item.optString("pubDate");
                        if (!pubDate.isEmpty()) {
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                LocalDateTime dateTime = LocalDateTime.parse(pubDate, formatter);
                                article.setDate(dateTime);
                            } catch (DateTimeParseException e) {
                                System.out.println("날짜 파싱 오류: " + pubDate);
                                article.setDate(null); // 파싱 실패 시 null 또는 기본값으로 설정
                            }
                        }

                        // 출처 설정
                        article.setSource(item.optString("originallink", null)); // 출처 (없으면 NULL)

                        // 조회수 기본값 설정
                        article.setHits(0L); // 조회수는 기본값으로 0을 설정

                        // Shorts 객체 설정
                        Shorts shorts = new Shorts();
                        article.setShorts(shorts);

                        articles.add(article);
                    }

                    // 데이터베이스에 저장
                    try {
                        articleRepository.saveAll(articles);
                    } catch (Exception e) {
                        System.out.println("데이터베이스 저장 오류: " + e.getMessage());
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    System.out.println("JSON 파싱 오류: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("API 요청 실패: 상태 코드 " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
