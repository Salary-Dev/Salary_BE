package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Domain.Word;
import salary_BE.salary.Domain.WordLike;

import java.util.List;
import java.util.Optional;

public interface WordLikeRepository extends JpaRepository<WordLike, Long> {
    List<WordLike> findByUser(User user);

    Optional<WordLike> findByWordId(Long wordId);

    // 유저와 word_bookmark 조건으로 WordLike 조회
    @Query("SELECT wl FROM WordLike wl WHERE wl.user = :user AND wl.wordBookmark = :bookmark")
    List<WordLike> findByUserAndWordBookmark(@Param("user") User user, @Param("bookmark") boolean bookmark);

    Optional<WordLike> findTopByUserOrderByLikeDateDesc(User user);

    @Query("SELECT wl FROM WordLike wl ORDER BY function('RAND')")
    List<WordLike> findRandomWordLikesLimit12(); //무작위로 12개 북마킹 단어 선정
}
