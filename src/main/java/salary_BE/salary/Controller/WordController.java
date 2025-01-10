package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Service.WordService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/words")
    public WordmainDto getWordById(@RequestParam Long word_id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        String loginId = userDetails.getUser().getLoginId();
        User user = userDetails.getUser();

        WordmainDto response = wordService.getWordById(word_id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 word_id에 맞는 데이터가 없습니다."));

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("GET /words 응답 시간: " + (endTime - startTime) + "ms");

        return response;
    }

    @GetMapping("/words/search")
    public List<WordmainDto> getWordByword(@RequestParam String word, @AuthenticationPrincipal CustomUserDetails userDetails) {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        User user = null;
        if (userDetails != null) {
            user = userDetails.getUser();
        }

        List<WordmainDto> results = wordService.getWordsByWordContaining(word, user);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("해당 word에 맞는 데이터가 없습니다.");
        }

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("GET /words/search 응답 시간: " + (endTime - startTime) + "ms");

        return results;
    }

    @GetMapping("/words/recommand")
    public List<WordmainDto> getRandomWords(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록

        User user = userDetails.getUser();
        List<WordmainDto> response = wordService.getRandomWords(user);

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        System.out.println("GET /words/recommand 응답 시간: " + (endTime - startTime) + "ms");

        return response;
    }
}
