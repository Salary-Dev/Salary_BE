package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.WordLike;
import salary_BE.salary.Service.WordLikeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class WordLikeController {

    private final WordLikeService wordLikeService;

    // 단어 북마크 저장
    @PostMapping("/wordbook")
    public ResponseEntity<Map<String, String>> addWordToWordBook(@RequestParam Long word_id) {

        wordLikeService.addWordToWordBook(word_id);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 단어장 조회
    @GetMapping("/wordbook")
    public ResponseEntity<List<Map<String, Object>>> getUserLikedWords() {

        List<Map<String, Object>> likedWords = wordLikeService.getUserLikedWords();

        return ResponseEntity.ok(likedWords);
    }

    // 단어장 삭제
    @DeleteMapping("/wordbook")
    public ResponseEntity<Map<String, String>> deleteWordLike(@RequestParam Long word_id) {
        // 서비스 호출
        wordLikeService.deleteWordLike(word_id);

        // 성공 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 단어장 리마인더
    @GetMapping("/wordbook/reminder")
    public ResponseEntity<List<Map<String, String>>> getRandomWords() {
        // WordRemindingDto 리스트를 JSON 배열 형식으로 변환
        List<Map<String, String>> randomWords = wordLikeService.getRandomWords().stream()
                .map(wordRemindingDto -> Map.of(
                        "word", wordRemindingDto.getWord(),
                        "mean", wordRemindingDto.getMean()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(randomWords);
    }

    // 단어 학습 여부
    @PostMapping("/today-word/update-status")
    public ResponseEntity<Map<String, String>> updateWord(@RequestParam Long word_id) {
        if (word_id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "'word_id' 파라미터 찾을 수 없습니다."));
        }

        wordLikeService.completeWord(word_id);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}