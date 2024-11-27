package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.AttendanceService;
import salary_BE.salary.Service.WordService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    // 날짜별 출석률 조회
    @GetMapping("/attendance/status")
    public ResponseEntity<Map<String, Object>> getAttendanceStatus(@RequestParam("attendance_date") String attendanceDate, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new IllegalArgumentException("로그인 ID에 해당하는 사용자가 없습니다.");
        }
        Attendance attendance = attendanceService.getAttendanceByDate(attendanceDate, user.getId());

        // 조회된 출석 정보를 JSON 형태로 반환
        Map<String, Object> response = new HashMap<>();
        response.put("attendance_id", attendance.getId());
        response.put("attendance_state", attendance.getAttendanceState());

        return ResponseEntity.ok(response);
    }

    // 오늘 학습 단어 조회
    @GetMapping("/today-word")
    public ResponseEntity<Map<String, Long>> getTodayWord(@AuthenticationPrincipal CustomUserDetails userDetails) {

        String loginId = userDetails.getUser().getLoginId();
        User user = userRepository.findByLoginId(loginId);
        Long word_id = attendanceService.getTodayWord(user.getId());

        Map<String, Long> response = new HashMap<>();
        response.put("word_id", word_id);
        return ResponseEntity.ok(response);
    }
}
