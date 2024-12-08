package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Service.WordService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;
    private final UserRepository userRepository;

    @GetMapping("/words")
    public WordmainDto getWordById(@RequestParam Long word_id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);

        return wordService.getWordById(word_id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 word_id에 맞는 데이터가 없습니다."));
    }

    @GetMapping("/words/search")
    public List<WordmainDto> getWordByword(@RequestParam String word, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = null;

        // 인증된 사용자만 User 객체 가져오기 (search 의 경우 인증 필요 없음)
        if (userDetails != null) {
            user = userDetails.getUser();
        }

        // 단어 검색 처리
        List<WordmainDto> results = wordService.getWordsByWordContaining(word, user);

        if (results.isEmpty()) {
            throw new IllegalArgumentException("해당 word에 맞는 데이터가 없습니다.");
        }

        return results;
    }

    @GetMapping("/words/recommand")
    public List<WordmainDto> getRandomWords(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return wordService.getRandomWords(user);
    }

}
