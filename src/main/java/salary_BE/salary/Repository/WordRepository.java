package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Domain.WordLike;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByWord(String word);

    @Query("SELECT w FROM Word w ORDER BY function('RAND')")
    List<Word> findRandomWordsLimit7(); //무작위로 7개 북마킹 단어 선정

    // 실시간 단어 검색
    List<Word> findByWordContaining(String keyword);
    List<Word> findAll();
}
