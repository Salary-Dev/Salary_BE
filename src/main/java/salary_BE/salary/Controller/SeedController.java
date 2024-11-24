package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Service.AttendanceService;
import salary_BE.salary.Service.SeedService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SeedController {

    private final SeedService seedService;
    private final AttendanceService attendanceService;

    // 시드 변경
    @PatchMapping("/seed/update")
    public ResponseEntity<Map<String, String>> updateSeed(@RequestBody Map<String, Integer> requestBody) {

        Integer seed_earned = requestBody.get("seed_earned");
        Integer seed_used = requestBody.get("seed_used");

        seedService.updateSeed(seed_earned, seed_used);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 시드 조회
    @GetMapping("/seed")
    public ResponseEntity<Map<String, Object>> getSeedLog(@RequestParam("date") String attendanceDate) {
        List<Attendance> attendances = attendanceService.getAttendanceByMonth(attendanceDate);

        // 조회된 출석 데이터를 JSON 형태로 변환
        Map<String, Object> response = new HashMap<>();
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
