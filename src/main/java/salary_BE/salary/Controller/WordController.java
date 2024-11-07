package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Service.WordService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;
    private final WordRepository wordRepository;

    @GetMapping("/words")
    public Word getWordById(@RequestParam Long word_id) {
        Optional<Word> word = wordService.getWordById(word_id);
        if (word.isPresent()) {
            return word.get();
        } else {
            throw new IllegalArgumentException("해당 word_id에 맞는 데이터가 없습니다.");
        }
    }

}
