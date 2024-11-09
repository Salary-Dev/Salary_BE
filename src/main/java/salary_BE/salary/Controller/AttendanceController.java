package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Service.AttendanceService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    @GetMapping("/attendance/status")
    public ResponseEntity<Map<String, Object>> getAttendanceStatus(@RequestParam("attendance_date") String attendanceDate) {
        Attendance attendance = attendanceService.getAttendanceByDate(attendanceDate);

        // 조회된 출석 정보를 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("attendance_id", attendance.getId());
        response.put("attendance_state", attendance.getAttendanceState());

        return ResponseEntity.ok(response);
    }
}
