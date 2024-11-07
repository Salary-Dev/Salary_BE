/*
package salary_BE.salary.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrendQuizService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String openAiCompletionUrl = "https://api.openai.com/v1/chat/completions";
    private final String modelName = "gpt-4-turbo";
    private final String fixedPrompt = "오늘의 경제/금융 트렌드를 반영한 4지 선다 퀴즈를 존댓말로 만들어 주세요. (연도 언급 x)" + "형식: {\"trend_quiz\": \"문제 내용\", \"correct\": \"정답\", \"incorrect\": [\"오답1\", \"오답2\", \"오답3\"], \"explanation\": \"해설\"}.";

    private final int maxTokens = 500;  // 길이가 길어질 시 늘려야 함
    private final RestTemplate restTemplate = new RestTemplate();

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
}
*
 */
