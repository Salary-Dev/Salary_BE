package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.TodayStudy;

import java.util.Optional;

public interface TodayStudyRepository extends JpaRepository<TodayStudy, Long> {

    TodayStudy findByAttendanceId(Long attendance_id);
}
