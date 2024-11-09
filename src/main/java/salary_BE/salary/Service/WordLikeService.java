package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.WordLikeDto;
import salary_BE.salary.DTO.WordRemindingDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Domain.WordLike;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Repository.WordLikeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class WordLikeService {

    private final WordRepository wordRepository;
    private final WordLikeRepository wordLikeRepository;
    private final UserRepository userRepository;

    public WordLike addWordToWordBook(String wordName) {
        // 단어 이름으로 Word 엔티티 조회
        Word word = wordRepository.findByWord(wordName)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어를 찾을 수 없습니다: " + wordName));

        // WordLike 엔티티 생성 및 설정
        WordLike wordLike = new WordLike();
        wordLike.setWord(word);
        wordLike.setWordBookmark(true);
        wordLike.setLikeDate(LocalDateTime.now());

        // WordLike 엔티티 저장
        return wordLikeRepository.save(wordLike);
    }

    public List<WordLikeDto> getUserLikedWords() {

        // 해당 유저가 좋아요한 단어 조회
        return wordLikeRepository.findAll().stream()
                .map(wordLike -> new WordLikeDto(
                        wordLike.getWord().getId(),
                        wordLike.getWord().getWord(),
                        wordLike.getLikeDate()
                ))
                .collect(Collectors.toList());
    }

    public void deleteWordLike(Long word_id) {
        // wordId로 WordLike 엔티티 조회
        WordLike wordLike = wordLikeRepository.findByWordId(word_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 단어는 북마크되지 않았습니다."));

        // WordLike 엔티티 삭제
        wordLikeRepository.delete(wordLike);
    }

    public List<WordRemindingDto> getRandomWords() {
        // 무작위로 12개의 WordLike 엔티티를 가져와 WordDto로 변환
        return wordLikeRepository.findRandomWordLikesLimit12().stream()
                .map(wordLike -> new WordRemindingDto(wordLike.getWord().getWord(), wordLike.getWord().getMean()))
                .collect(Collectors.toList());
    }
}

