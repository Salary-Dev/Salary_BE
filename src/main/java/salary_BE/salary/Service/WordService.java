package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.WordmainDto;
import salary_BE.salary.Domain.ArticleWordMapping;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.DTO.WordmainDto;
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

    public Optional<WordmainDto> getWordById(Long wordId) {
        Optional<Word> word = wordRepository.findById(wordId);

        if (word.isPresent()) {
            List<String> urls = articleWordMappingRepository.findByWord(word.get())
                    .stream()
                    .map(mapping -> mapping.getArticle().getUrl())
                    .collect(Collectors.toList());

            return Optional.of(new WordmainDto(
                    word.get().getId(),
                    word.get().getWord(),
                    word.get().getMean(),
                    word.get().getStory1(),
                    word.get().getStory2(),
                    word.get().getStory3(),
                    word.get().getExample(),
                    urls
            ));
        }
        return Optional.empty();
    }
}
