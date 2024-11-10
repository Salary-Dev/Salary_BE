package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Repository.WordRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final WordRepository wordRepository;

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

    // 오늘 학습 단어 조회
    public Long getTodayWord() {
        User currentUser = userService.getCurrentUser(); // 현재 사용자 조회

        // 출석 정보 가져오기
        Attendance attendance = attendanceRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("출석 정보가 없습니다."));

        // 마지막 학습 단어 확인
        Long lastWordId = attendance.getLastWordId();

        // 다음 학습 단어 ID 반환
        return wordRepository.findById(lastWordId + 1)
                .orElseThrow(() -> new RuntimeException("모든 단어를 학습했습니다."))
                .getId();
    }
}
