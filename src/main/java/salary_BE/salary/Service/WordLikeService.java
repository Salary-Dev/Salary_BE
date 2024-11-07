package salary_BE.salary.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import salary_BE.salary.Repository.WordLikeRepository;

@Service
public class WordLikeService {

    private final WordLikeRepository wordLikeRepository;

    @Autowired
    public WordLikeService(WordLikeRepository wordLikeRepository) {
        this.wordLikeRepository = wordLikeRepository;
    }

}
