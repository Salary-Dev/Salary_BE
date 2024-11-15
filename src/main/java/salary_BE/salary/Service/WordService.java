package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.ArticleWordMapping;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.WordLikeRepository;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Repository.ArticleWordMappingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final ArticleWordMappingRepository articleWordMappingRepository;
    private final WordLikeRepository wordLikeRepository;
    private final UserService userService;

    public Optional<WordmainDto> getWordById(Long wordId) {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
        Optional<Word> word = wordRepository.findById(wordId);

        return word.map(wordEntity -> {
            List<String> urls = articleWordMappingRepository.findByWord(wordEntity)
                    .stream()
                    .map(mapping -> mapping.getArticle().getUrl())
                    .collect(Collectors.toList());

            // 현재 사용자가 이 단어를 북마크했는지 확인
            boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, wordEntity);

            return new WordmainDto(
                    wordEntity.getId(),
                    wordEntity.getWord(),
                    wordEntity.getMean(),
                    wordEntity.getStory1(),
                    wordEntity.getStory2(),
                    wordEntity.getStory3(),
                    wordEntity.getExample(),
                    urls,
                    isSavedByUser
            );
        });
    }

    public Optional<WordmainDto> getWordByword(String wordSearch) {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
        Optional<Word> word = wordRepository.findByWord(wordSearch);

        return word.map(wordEntity -> {
            List<String> urls = articleWordMappingRepository.findByWord(wordEntity)
                    .stream()
                    .map(mapping -> mapping.getArticle().getUrl())
                    .collect(Collectors.toList());

            // 현재 사용자가 이 단어를 북마크했는지 확인
            boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, wordEntity);

            return new WordmainDto(
                    wordEntity.getId(),
                    wordEntity.getWord(),
                    wordEntity.getMean(),
                    wordEntity.getStory1(),
                    wordEntity.getStory2(),
                    wordEntity.getStory3(),
                    wordEntity.getExample(),
                    urls,
                    isSavedByUser
            );
        });
    }

    public List<WordmainDto> getRandomWords() {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
        List<Word> words = wordRepository.findRandomWordsLimit7();

        return words.stream()
                .limit(7)
                .map(word -> {
                    List<String> urls = articleWordMappingRepository.findByWord(word)
                            .stream()
                            .map(mapping -> mapping.getArticle().getUrl())
                            .collect(Collectors.toList());

                    // 현재 사용자가 이 단어를 북마크했는지 확인
                    boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, word);

                    return new WordmainDto(
                            word.getId(),
                            word.getWord(),
                            word.getMean(),
                            word.getStory1(),
                            word.getStory2(),
                            word.getStory3(),
                            word.getExample(),
                            urls,
                            isSavedByUser
                    );
                })
                .collect(Collectors.toList());
    }

    // 실시간 단어 검색
    public List<WordmainDto> getWordsByWordContaining(String wordSearch) {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
        // 입력된 단어를 포함하는 모든 단어 조회
        List<Word> words = wordRepository.findByWordContaining(wordSearch);

        return words.stream()
                .map(word -> {
                    List<String> urls = articleWordMappingRepository.findByWord(word)
                            .stream()
                            .map(mapping -> mapping.getArticle().getUrl())
                            .collect(Collectors.toList());

                    // 현재 사용자가 이 단어를 북마크했는지 확인
                    boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, word);

                    return new WordmainDto(
                            word.getId(),
                            word.getWord(),
                            word.getMean(),
                            word.getStory1(),
                            word.getStory2(),
                            word.getStory3(),
                            word.getExample(),
                            urls,
                            isSavedByUser
                    );
                })
                .collect(Collectors.toList());
    }
}
