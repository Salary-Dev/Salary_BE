package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.ArticleDto;
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


        if (word.isPresent()) {
            List<ArticleDto> articles = articleWordMappingRepository.findByWord(word.get())
                    .stream()
                    .map(mapping -> new ArticleDto(
                            mapping.getArticle().getUrl(),
                            mapping.getArticle().getTitle()
                    ))
                    .collect(Collectors.toList());

            // 현재 사용자가 이 단어를 북마크했는지 확인
            boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, wordEntity);
            return Optional.of(new WordmainDto(
                    word.get().getId(),
                    word.get().getWord(),
                    word.get().getMean(),
                    word.get().getStory1(),
                    word.get().getStory2(),
                    word.get().getStory3(),
                    word.get().getExample(),
                    articles
            ));
        }
        return Optional.empty();
    }

    public Optional<WordmainDto> getWordByword(String wordSearch) {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
        Optional<Word> word = wordRepository.findByWord(wordSearch);

        if (word.isPresent()) {
            List<ArticleDto> articles = articleWordMappingRepository.findByWord(word.get())
                    .stream()
                    .map(mapping -> new ArticleDto(
                            mapping.getArticle().getUrl(),
                            mapping.getArticle().getTitle()
                    ))
                    .collect(Collectors.toList());

            boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, wordEntity);
            return Optional.of(new WordmainDto(
                    word.get().getId(),
                    word.get().getWord(),
                    word.get().getMean(),
                    word.get().getStory1(),
                    word.get().getStory2(),
                    word.get().getStory3(),
                    word.get().getExample(),
                    articles,
                    isSavedByUser
            ));
        }
        return Optional.empty();
    }

    public List<WordmainDto> getRandomWords() {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
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
                    boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, word);

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
    public List<WordmainDto> getWordsByWordContaining(String wordSearch) {
        User currentUser = userService.getCurrentUser(); // 현재 유저 가져오기
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
                    boolean isSavedByUser = wordLikeRepository.existsByUserAndWordAndWordBookmarkTrue(currentUser, word);

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
