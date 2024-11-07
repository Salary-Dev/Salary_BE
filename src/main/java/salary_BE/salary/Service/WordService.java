package salary_BE.salary.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Repository.WordRepository;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public Optional<Word> getWordById(Long wordId) {
        return wordRepository.findById(wordId);
    }
}
