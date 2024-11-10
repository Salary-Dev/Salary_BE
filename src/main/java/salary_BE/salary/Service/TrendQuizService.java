package salary_BE.salary.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.TodayStudy;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.TodayStudyRepository;
import salary_BE.salary.Repository.UserRepository;

import javax.swing.plaf.ToolTipUI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrendQuizService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String openAiCompletionUrl = "https://api.openai.com/v1/chat/completions";
    private final String modelName = "gpt-4-turbo";
    private final String fixedPrompt = "오늘의 경제/금융 트렌드를 반영한 4지 선다 퀴즈를 존댓말로 만들어 주세요. (연도 언급 x)" + "형식: {\"trend_quiz\": \"문제 내용\", \"correct\": \"정답\", \"incorrect\": [\"오답1\", \"오답2\", \"오답3\"], \"explanation\": \"해설\"}.";

    private final int maxTokens = 500;  // 길이가 길어질 시 늘려야 함
    private final RestTemplate restTemplate = new RestTemplate();
    private final TodayStudyRepository todayStudyRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    // 트렌드 퀴즈 요청
    public String getTrendQuiz() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>(); // 형식 지정
        requestBody.put("model", modelName);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", fixedPrompt)
        ));
        requestBody.put("max_tokens", maxTokens);

        HttpEntity<String> request;
        try {
            String jsonRequest = new ObjectMapper().writeValueAsString(requestBody);
            request = new HttpEntity<>(jsonRequest, headers);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JSON request body", e);
        }

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    openAiCompletionUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // 응답에서 content 내용만 추출
            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            JsonNode contentNode = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content");

            // JSON 문자열 그대로 반환
            return contentNode.asText();

        } catch (Exception e) {  // open ai 예외 처리
            e.printStackTrace();
            throw new RuntimeException("Failed to call OpenAI API or parse response", e);
        }
    }

    // 트렌드 퀴즈 학습 여부
    @Transactional
    public void completeTrendQuiz(boolean trend) {
        User currentUser = userService.getCurrentUser(); // 사용자 식별
        LocalDate todayDate = getCurrentDate();
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), todayDate); // 사용자의 출석 정보 가져오기

        // user_id + attendance_date로 검색 후 없으면 정보 초기화
        attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), todayDate);
        if (attendance == null) {
            attendance = new Attendance();
            // 1. 출석률 테이블 [attendance_date] 속성에 학습 날짜 저장
            attendance.setAttendanceDate(todayDate);
            attendance.setAttendanceState(0); // 학습 상태 초기화
            attendance = attendanceRepository.save(attendance);
        }

        // 오늘 학습 정보 가져오기 없으면 정보 초기화
        TodayStudy todayStudy = todayStudyRepository.findByAttendanceId(attendance.getId());
        if (todayStudy == null) {
            todayStudy = new TodayStudy();
            todayStudy.setAttendance(attendance); // 출석률 테이블과 연결
            todayStudy.setTrend(false); // 트렌드 퀴즈 학습 여부 초기화
            todayStudy = todayStudyRepository.save(todayStudy);
        }


        // 2. 오늘 학습 테이블 [트렌드] 속성에 학습 여부 반영
        todayStudy.setTrend(trend);
        todayStudyRepository.save(todayStudy); // 학습 상태 저장

        int currentState = attendance.getAttendanceState();
        // 3. 트렌드 퀴즈 학습 시 출석률 테이블 [attendance_state] 속성 + 1
        attendance.setAttendanceState(currentState + 1);
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
        return java.time.LocalDate.now();
    }

}
