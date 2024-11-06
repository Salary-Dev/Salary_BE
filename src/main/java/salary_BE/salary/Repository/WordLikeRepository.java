package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.WordLike;

import java.util.Optional;

public interface WordLikeRepository extends JpaRepository<WordLike, Long> {

    Optional<WordLike> findByUserWordMappingId(Long user_word_mapping_id);
    Optional<WordLike> findByUserId(Long user_id);
    Optional<WordLike> findByWordId(Long word_id);


}
