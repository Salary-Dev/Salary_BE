package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.templateparser.text.AbstractTextTemplateParser;
import salary_BE.salary.Domain.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Attendance findByUserIdAndAttendanceDate(Long user_id, LocalDate attendance_date);
    List<Attendance> findAllByUserId(Long userId);

    // 가장 최근 Attendance -> 스케줄러에서 사용됨
    Optional<Attendance> findTopByUserIdOrderByAttendanceDateDesc(Long userId);

}
