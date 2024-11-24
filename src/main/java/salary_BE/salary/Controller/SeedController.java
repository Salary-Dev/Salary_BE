package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Service.AttendanceService;
import salary_BE.salary.Service.SeedService;

import java.util.HashMap;
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
        Attendance attendance = attendanceService.getAttendanceByDate(attendanceDate); // 기존 구현된 함수 이용

        // 조회된 시드 로그를 JSON 형태로 변환
        Map<String, Object> response = new HashMap<>();
        response.put("attendance_date", attendance.getAttendanceDate());
        response.put("today_seed", attendance.getTodaySalaryPoint());
        response.put("today_seed_earned", attendance.getTodaySalaryPoint_earned());
        response.put("today_seed_used", attendance.getTodaySalaryPoint_used());

        return ResponseEntity.ok(response);
    }
}
