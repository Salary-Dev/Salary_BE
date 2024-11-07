package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.ArticleWordMapping;
import salary_BE.salary.Domain.Word;

import java.util.List;

public interface ArticleWordMappingRepository extends JpaRepository<ArticleWordMapping, Long> {
    List<ArticleWordMapping> findByWord(Word word);
}
