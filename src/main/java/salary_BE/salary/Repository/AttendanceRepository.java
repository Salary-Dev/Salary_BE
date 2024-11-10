package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thymeleaf.templateparser.text.AbstractTextTemplateParser;
import salary_BE.salary.Domain.Attendance;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Attendance findByUserIdAndAttendanceDate(Long user_id, LocalDate attendance_date);
    Optional<Attendance> findByUserId(Long user_id);

}
