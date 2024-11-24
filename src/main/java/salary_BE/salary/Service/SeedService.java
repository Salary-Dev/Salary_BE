package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class SeedService {

    private final UserService userService;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    // 시드 변경
    public String updateSeed(Integer seed_earned, Integer seed_used) {
        User currentUser = userService.getCurrentUser(); // 현재 사용자 조회
        LocalDate todayDate = getCurrentDate();

        // 1. [출석률] 테이블 해당 날짜의 포인트 변경 (today_seed)
       Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), todayDate);
       Integer previousSeed = attendance.getTodaySalaryPoint();

       // 기존 테이블 초기화 시 시드의 값은 Null
       if (previousSeed == null) {
           previousSeed = 0; // 초기화
       }

       attendance.setTodaySalaryPoint(previousSeed + seed_earned - seed_used);  // 기존에 변경

        // 2. [회원] 테이블 샐러리 점수 변경 (total_seed)
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Integer previousTotalSeed = user.getSalaryPoint();

        // 유저 처음 추가 시 시드의 값은 Null
        if (previousTotalSeed == null) {
            previousTotalSeed = 0; // 초기화
        }

        user.setSalaryPoint(previousTotalSeed + seed_earned - seed_used); // 기존에 변경

        return "success";
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
