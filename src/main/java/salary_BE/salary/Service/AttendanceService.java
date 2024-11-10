package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    // 날짜별 출석률 조회
    public Attendance getAttendanceByDate(String attendanceDate) {
        User currentUser = userService.getCurrentUser(); // 현재 사용자 조회

        LocalDate date = LocalDate.parse(attendanceDate);
        try {
            date = LocalDate.parse(attendanceDate); // 'YYYY-MM-DD' 형식 확인
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("YYYY-MM-DD' 형식으로 입력하세요.");
        }
        return attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), date);
    }
}
