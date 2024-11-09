package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.WordLike;
import salary_BE.salary.Service.WordLikeService;

import java.util.List;

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

}