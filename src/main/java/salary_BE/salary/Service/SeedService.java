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
    public String updateSeed(Integer seed_earned, Integer seed_used, User user) {
        LocalDate todayDate = getCurrentDate();

        // 1. [출석률] 테이블 해당 날짜의 포인트 변경 (today_seed)
       Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(user.getId(), todayDate);
       Integer previousSeed = attendance.getTodaySalaryPoint();

       // 기존 테이블 초기화 시 시드의 값은 Null
       if (previousSeed == null)
           previousSeed = 0; // 초기화

       attendance.setTodaySalaryPoint(previousSeed + seed_earned - seed_used);  // 기존에 변경
        attendanceRepository.save(attendance);

        // 포인트 적립/사용 내역 저장
        Integer previousSeed_earned = attendance.getTodaySalaryPoint_earned();
        Integer previousSeed_used = attendance.getTodaySalaryPoint_used();

        if (previousSeed_earned == null)
            previousSeed_earned = 0; // 초기화

        attendance.setTodaySalaryPoint_earned(previousSeed_earned + seed_earned);

        if (previousSeed_used == null)
            previousSeed_used = 0; // 초기화

        attendance.setTodaySalaryPoint_used(previousSeed_used - seed_used);

        // 2. [회원] 테이블 샐러리 점수 변경 (total_seed)
        User userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Integer previousTotalSeed = userEntity.getSalaryPoint();

        // 유저 처음 추가 시 시드의 값은 Null
        if (previousTotalSeed == null)
            previousTotalSeed = 0; // 초기화

        user.setSalaryPoint(previousTotalSeed + seed_earned - seed_used); // 기존에 변경
        userRepository.save(userEntity);

        return "success";
    }

    // 오늘 날짜 가져오기
    private LocalDate getCurrentDate() {
        return LocalDate.now(ZoneId.of("Asia/Seoul"));
    }
}
