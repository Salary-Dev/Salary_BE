package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Domain.WordLike;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WordLikeRepository extends JpaRepository<WordLike, Long> {

    List<WordLike> findByUser(User user);
    Optional<WordLike> findByWord(Word word);
    Optional<WordLike> findByWordId(Long wordId);

    @Query("SELECT wl FROM WordLike wl ORDER BY function('RAND')")
    List<WordLike> findRandomWordLikesLimit12(); //무작위로 12개 북마킹 단어 선정

    Optional<WordLike> findTopByUserOrderByWord(User user);  // 단어 학습 업데이트를 위해 가장 최근에 학습한 단어 선정

}
