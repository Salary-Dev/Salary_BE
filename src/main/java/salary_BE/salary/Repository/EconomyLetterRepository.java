package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.EconomyLetter;
import salary_BE.salary.Service.EconomyLetterService;

import java.util.List;

public interface EconomyLetterRepository extends JpaRepository<EconomyLetter, Long> {

    List<EconomyLetter> findByEditor_Id(Long editorId);
    List<EconomyLetter> findAll();
}
