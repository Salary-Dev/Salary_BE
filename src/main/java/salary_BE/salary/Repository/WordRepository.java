package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.Word;

public interface WordRepository extends JpaRepository<Word, Long> {

}
