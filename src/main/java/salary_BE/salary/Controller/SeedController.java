package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.AttendanceService;
import salary_BE.salary.Service.SeedService;
import salary_BE.salary.Service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SeedController {

    private final SeedService seedService;
    private final AttendanceService attendanceService;
    private final UserService userService;
    private final UserRepository userRepository;

    // 시드 변경
    @PatchMapping("/seed/update")
    public ResponseEntity<Map<String, String>> updateSeed(@RequestBody Map<String, Integer> requestBody, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);

        Integer seed_earned = requestBody.get("seed_earned");
        Integer seed_used = requestBody.get("seed_used");

        seedService.updateSeed(seed_earned, seed_used, user);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 시드 조회
    @GetMapping("/seed")
    public ResponseEntity<Map<String, Object>> getSeedLog(@RequestParam("date") String attendanceDate,@AuthenticationPrincipal CustomUserDetails userDetails) {
        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        List<Attendance> attendances = attendanceService.getAttendanceByMonth(attendanceDate, user.getId());


        // total_seed 조회를 위한 현재 사용자 가져오기
        User currentUser = userService.getCurrentUser();
        Integer totalSeed = currentUser.getSalaryPoint();

        // 조회된 출석 데이터를 JSON 형태로 변환
        Map<String, Object> response = new HashMap<>();
        response.put("total_seed", totalSeed);
        response.put("attendance_logs", attendances.stream().map(attendance -> {
            Map<String, Object> log = new HashMap<>();
            log.put("attendance_date", attendance.getAttendanceDate());
            log.put("today_seed", attendance.getTodaySalaryPoint());
            log.put("today_seed_earned", attendance.getTodaySalaryPoint_earned());
            log.put("today_seed_used", attendance.getTodaySalaryPoint_used());
            return log;
        }).toList());

        return ResponseEntity.ok(response);
    }
}
