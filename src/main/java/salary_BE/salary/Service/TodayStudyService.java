package salary_BE.salary.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.TodayStudy;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.TodayStudyRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodayStudyService {

    private final UserService userService;
    private final TodayStudyRepository todayStudyRepository;
    private final AttendanceRepository attendanceRepository;

    // 오늘 학습 과목 조회
    public Map<String, Boolean> getTodayStudyStatus() {

        User currentUser = userService.getCurrentUser(); // 현재 사용자 조회

        // 오늘 날짜 기준으로 정보 가져오기
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // [출석률] 테이블 연결해 출석 정보 가져오기
        Attendance attendance = attendanceRepository.findByUserIdAndAttendanceDate(currentUser.getId(), today);
        if (attendance == null) {
            throw new RuntimeException("출석 정보가 없습니다.");
        }

        // 오늘 학습 상태 가져오기
        TodayStudy todayStudy = todayStudyRepository.findByAttendanceId(attendance.getId());

        Map<String, Boolean> response = new HashMap<>();
        response.put("word", todayStudy.isWord());
        response.put("trend", todayStudy.isTrend());
        response.put("article", todayStudy.isArticle());

        return response;
    }
}
