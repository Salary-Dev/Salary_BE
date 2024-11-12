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
import java.util.List;

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

        // current User의 userId와 매칭되는 Attendance 데이터 가져오기
        List<Attendance> userAttendances = attendanceRepository.findAllByUserId(currentUser.getId());

        if (userAttendances.isEmpty()) {
            throw new RuntimeException("출석 정보가 없습니다.");
        }

        // 가장 큰 last_word_Id 찾기
        Long maxLastWordId = userAttendances.stream()
                .map(Attendance::getLastWordId)
                .max(Long::compareTo)
                .orElseThrow(() -> new RuntimeException("출석 정보에서 마지막 학습 단어 ID를 찾을 수 없습니다."));

        // 다음 학습할 단어 ID 반환
        return maxLastWordId + 1;
    }
}
