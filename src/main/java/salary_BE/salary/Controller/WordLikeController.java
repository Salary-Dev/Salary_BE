package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.WordLike;
import salary_BE.salary.Service.WordLikeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class WordLikeController {

    private final WordLikeService wordLikeService;

    @PostMapping("/wordbook")
    public ResponseEntity<?> addWordToWordBook(@RequestParam String word) {
        wordLikeService.addWordToWordBook(word);
        return ResponseEntity.ok().body("{status: success}");
    }
    @GetMapping("/wordbook")
    public List<WordLikeDto> getUserLikedWords() {
        return wordLikeService.getUserLikedWords();
    }
    @DeleteMapping("/wordbook")
    public ResponseEntity<?> deleteWordLike(@RequestParam Long word_id) {
        wordLikeService.deleteWordLike(word_id);
        return ResponseEntity.ok().body("{status: success}");
    }
    @GetMapping("/wordbook/reminder")
    public List<WordRemindingDto> getRandomWords() {
        return wordLikeService.getRandomWords();
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