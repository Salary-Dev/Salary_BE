package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import salary_BE.salary.DTO.ArticleDto;
import salary_BE.salary.Domain.*;

import salary_BE.salary.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ArticleService {

    private final ArticleRepository articleRepository;
    private final WordRepository wordRepository;
    private final ArticleWordMappingRepository articleWordMappingRepository;
    private final AttendanceRepository attendanceRepository;
    private final TodayStudyRepository todayStudyRepository;

    @Value("${X-Naver-Client-Id}")
    private String clientId;

    @Value("${X-Naver-Client-Secret}")
    private String clientSecret;

    public void fetchAndSaveNewsArticles(String query, int display) {
        String url = "https://openapi.naver.com/v1/search/news.json?query=" + query + "&sort = sim"+ "&display="+display;

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
                try {
                    JSONObject jsonResponse = new JSONObject(response.getBody());
                    JSONArray items = jsonResponse.getJSONArray("items");

                    List<Article> articles = new ArrayList<>();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        Article article = new Article();

                        try {
                            article.setUrl(item.getString("link"));
                            article.setTitle(item.getString("title").replaceAll("<.*?>", "")); // HTML 태그 제거
                        } catch (JSONException e) {
                            System.out.println("링크 또는 제목 설정 오류: " + e.getMessage());
                        }

                        // 날짜 파싱 및 설정
                        String pubDate = item.optString("pubDate");
                        if (!pubDate.isEmpty()) {
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                LocalDateTime dateTime = LocalDateTime.parse(pubDate, formatter);
                                article.setDate(dateTime);
                            } catch (DateTimeParseException e) {
                                System.out.println("날짜 파싱 오류: " + pubDate);
                                article.setDate(null); // 파싱 실패 시 null 설정
                            }
                        }

                        // 출처 설정
                        try {
                            article.setSource(item.optString("originallink", null));
                        } catch (JSONException e) {
                            System.out.println("출처 설정 오류: " + e.getMessage());
                        }

                        // 조회수 기본값 설정
                        article.setHits(0L);

                        // Shorts 객체를 null로 설정
//                        article.setShorts(null);

                        articles.add(article);
                    }

                    // 데이터베이스에 저장
                    try {
                        articleRepository.saveAll(articles);
                        if(display != 2) {
                            List<Word> words = wordRepository.findAll();
                            for (Article article : articles) {
                                for (Word word : words) {
                                    if (article.getTitle().contains(word.getWord())) {
                                        System.out.println("매핑된 단어 발견 단어 이름 : "+word.getWord());
                                        ArticleWordMapping articleWordMapping = new ArticleWordMapping();
                                        articleWordMapping.setArticle(article);
                                        articleWordMapping.setWord(word);
                                        articleWordMappingRepository.save(articleWordMapping);
                                    }
                                }
                            }
                        }
                        //word-mapping api 호출시 적용
                        else{
                            Word word = wordRepository.findByWord(query)
                                    .orElseThrow(() -> new RuntimeException("Word not found: " + query));
                            for(Article article : articles){
                                    ArticleWordMapping articleWordMapping = new ArticleWordMapping();
                                    articleWordMapping.setArticle(article);
                                    articleWordMapping.setWord(word);
                                articleWordMappingRepository.save(articleWordMapping);
                                    System.out.println("저장된 단어 : "+word.getWord());
                            }
                        }

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

    // 아티클 기능 구현
    public List<ArticleDto> getRandomShorts() {
        List<Article> articles = articleRepository.findRandomArticles(10);
        return articles.stream().map(article -> {
            return new ArticleDto(
                    article.getUrl(),
                    article.getTitle(),
                    article.getSource()
            );
        }).collect(Collectors.toList());
    }

    // 아티클 학습 여부
    @Transactional
    public void completeArticle(boolean article, User user) {

        LocalDate todayDate = getCurrentDate();

        // user_id + attendance_date로 검색 후 없으면 정보 초기화
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(user.getId(), todayDate);
        if (attendance == null) {
            attendance = new Attendance();
            // 1. 출석률 테이블 [attendance_date] 속성에 학습 날짜 저장
            attendance.setAttendanceDate(todayDate);
            attendance.setUser(user); // 유저 정보 추가
            attendance.setAttendanceState(0); // 학습 상태 초기화
            attendance = attendanceRepository.save(attendance);
        }

        // 오늘 학습 정보 가져오기 없으면 정보 초기화
        TodayStudy todayStudy = todayStudyRepository.findByAttendanceId(attendance.getId());
        if (todayStudy == null) {
            todayStudy = new TodayStudy();
            todayStudy.setAttendance(attendance); // 출석률 테이블과 연결
            todayStudy.setArticle(false); // 아티클 학습 여부 초기화
            todayStudy = todayStudyRepository.save(todayStudy);
        }


        // 2. 오늘 학습 테이블 [아티클] 속성에 학습 여부 반영
        todayStudy.setArticle(article);
        todayStudyRepository.save(todayStudy); // 학습 상태 저장

        int currentState = attendance.getAttendanceState();
        // 3. 아티클 학습 시 출석률 테이블 [attendance_state] 속성 + 1
        attendance.setAttendanceState(currentState + 1);
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }

}
