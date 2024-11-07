package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Service.WordService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/words")
    public WordmainDto getWordById(@RequestParam Long word_id) {
        return wordService.getWordById(word_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 word_id에 맞는 데이터가 없습니다."));
    }
}
