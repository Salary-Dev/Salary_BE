package salary_BE.salary.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.ArticleDto;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.ArticleWordMapping;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Repository.WordLikeRepository;
import salary_BE.salary.Repository.WordRepository;
import salary_BE.salary.Repository.ArticleWordMappingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WordService {

    private final WordRepository wordRepository;
    private final ArticleWordMappingRepository articleWordMappingRepository;
    private final WordLikeRepository wordLikeRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public Optional<WordmainDto> getWordById(Long wordId, User user) {
        Optional<Word> word = wordRepository.findById(wordId);


        if (word.isPresent()) {
            Word wordEntity = word.get();
            List<ArticleDto> articles = articleWordMappingRepository.findByWord(word.get())
                    .stream()
                    .map(mapping -> new ArticleDto(
                            mapping.getArticle().getUrl(),
                            mapping.getArticle().getTitle()
                    ))
                    .collect(Collectors.toList());


            // 현재 사용자가 이 단어를 북마크했는지 확인
            boolean isSavedByUser = wordLikeRepository.existsByUserIdAndWordAndWordBookmarkTrue(user.getId(), wordEntity);
            return Optional.of(new WordmainDto(
                    wordEntity.getId(),
                    wordEntity.getWord(),
                    wordEntity.getMean(),
                    wordEntity.getStory1(),
                    wordEntity.getStory2(),
                    wordEntity.getStory3(),
                    wordEntity.getExample(),
                    articles,
                    isSavedByUser
            ));
        }
        return Optional.empty();
    }

    public Optional<WordmainDto> getWordByword(String wordSearch, User user) {
        Optional<Word> word = wordRepository.findByWord(wordSearch);

        if (word.isPresent()) {
            Word wordEntity = word.get();
            List<ArticleDto> articles = articleWordMappingRepository.findByWord(word.get())
                    .stream()
                    .map(mapping -> new ArticleDto(
                            mapping.getArticle().getUrl(),
                            mapping.getArticle().getTitle()
                    ))
                    .collect(Collectors.toList());

            boolean isSavedByUser = wordLikeRepository.existsByUserIdAndWordAndWordBookmarkTrue(user.getId(), wordEntity);
            return Optional.of(new WordmainDto(
                    wordEntity.getId(),
                    wordEntity.getWord(),
                    wordEntity.getMean(),
                    wordEntity.getStory1(),
                    wordEntity.getStory2(),
                    wordEntity.getStory3(),
                    wordEntity.getExample(),
                    articles,
                    isSavedByUser
            ));
        }
        return Optional.empty();
    }

    public List<WordmainDto> getRandomWords(User user) {
        List<Word> words = wordRepository.findRandomWordsLimit7();

        return words.stream()
                .limit(7)
                .map(word -> {
                    List<ArticleDto> articles = articleWordMappingRepository.findByWord(word)
                            .stream()
                            .map(mapping -> new ArticleDto(
                                    mapping.getArticle().getUrl(),
                                    mapping.getArticle().getTitle()
                            ))
                            .collect(Collectors.toList());

                    // 현재 사용자가 이 단어를 북마크했는지 확인
                    boolean isSavedByUser = wordLikeRepository.existsByUserIdAndWordAndWordBookmarkTrue(user.getId(), word);

                    return new WordmainDto(
                            word.getId(),
                            word.getWord(),
                            word.getMean(),
                            word.getStory1(),
                            word.getStory2(),
                            word.getStory3(),
                            word.getExample(),
                            articles,
                            isSavedByUser
                    );
                })
                .collect(Collectors.toList());
    }

    // 실시간 단어 검색
    public List<WordmainDto> getWordsByWordContaining(String wordSearch, User user) {
        // 입력된 단어를 포함하는 모든 단어 조회
        List<Word> words = wordRepository.findByWordContaining(wordSearch);

        return words.stream()
                .map(word -> {
                    List<ArticleDto> articles = articleWordMappingRepository.findByWord(word)
                            .stream()
                            .map(mapping -> new ArticleDto(
                                    mapping.getArticle().getUrl(),
                                    mapping.getArticle().getTitle()
                            ))
                            .collect(Collectors.toList());

                    // 현재 사용자가 이 단어를 북마크했는지 확인
                    boolean isSavedByUser = wordLikeRepository.existsByUserIdAndWordAndWordBookmarkTrue(user.getId(), word);

                    return new WordmainDto(
                            word.getId(),
                            word.getWord(),
                            word.getMean(),
                            word.getStory1(),
                            word.getStory2(),
                            word.getStory3(),
                            word.getExample(),
                            articles,
                            isSavedByUser

                    );
                })
                .collect(Collectors.toList());
    }
}
