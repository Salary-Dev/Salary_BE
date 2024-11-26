package salary_BE.salary.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import salary_BE.salary.DTO.CustomUserDetails;
import salary_BE.salary.Domain.TodayStudy;
import salary_BE.salary.Repository.TodayStudyRepository;
import salary_BE.salary.Service.TodayStudyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TodayStudyController {

    private final TodayStudyService todayStudyService;

    // 오늘 학습 과목 조회
    @GetMapping("attendance/today")
    public ResponseEntity<Map<String, Boolean>> getTodayStudyStatus(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();
        Map<String, Boolean> response = todayStudyService.getTodayStudyStatus(userId);
        return ResponseEntity.ok(response);
    }
}
