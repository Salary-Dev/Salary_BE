package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.WordLike;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.WordLikeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequiredArgsConstructor
public class WordLikeController {

    private final WordLikeService wordLikeService;
    private final UserRepository userRepository;

    // 단어 북마크 저장
    @PostMapping("/wordbook")
    public ResponseEntity<Map<String, String>> addWordToWordBook(@RequestParam Long word_id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new IllegalArgumentException("User가 없습니다.");
        }else if(word_id==null){
            throw new IllegalArgumentException("word가 없습니다.");
        }

        wordLikeService.addWordToWordBook(word_id, user);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 단어장 조회
    @GetMapping("/wordbook")
    public ResponseEntity<List<Map<String, Object>>> getUserLikedWords(@AuthenticationPrincipal CustomUserDetails userDetails) {


        String loginId = userDetails.getUser().getLoginId();

        List<Map<String, Object>> likedWords = wordLikeService.getUserLikedWords(loginId);

        return ResponseEntity.ok(likedWords);
    }

    // 단어장 삭제
    @DeleteMapping("/wordbook")
    public ResponseEntity<Map<String, String>> deleteWordLike(@RequestParam Long word_id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        // 서비스 호출
        wordLikeService.deleteWordLike(word_id,user);

        // 성공 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 단어장 리마인더
    @GetMapping("/wordbook/reminder")
    public ResponseEntity<List<Map<String, String>>> getRandomWords(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        // WordRemindingDto 리스트를 JSON 배열 형식으로 변환
        List<Map<String, String>> randomWords = wordLikeService.getRandomWords(user).stream()
                .map(wordRemindingDto -> Map.of(
                        "word", wordRemindingDto.getWord(),
                        "mean", wordRemindingDto.getMean()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(randomWords);
    }

    // 단어 학습 여부
    @PostMapping("/today-word/update-status")
    public ResponseEntity<Map<String, String>> updateWord(@RequestParam Long word_id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);

        if (word_id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "'word_id' 파라미터 찾을 수 없습니다."));
        }

        wordLikeService.completeWord(word_id, user);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}