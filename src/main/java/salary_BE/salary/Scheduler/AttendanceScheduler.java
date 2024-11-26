package salary_BE.salary.Scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.Controller.AttendanceController;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.TodayStudy;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.TodayStudyRepository;
import salary_BE.salary.Service.UserService;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class AttendanceScheduler {

    private final AttendanceRepository attendanceRepository;
    private final TodayStudyRepository todayStudyRepository;
    private final UserService userService;

     // 매일 자정 (00:00)에 실행
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void initializeDailyAttendance() {

        LocalDate todayDate = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // 모든 사용자에 대한 튜플 추가 및 초기화
        userService.getAllUsers().forEach(user -> {
            // 1. Attendance 초기화
            Attendance attendance = new Attendance();
            attendance.setAttendanceDate(todayDate);
            attendance.setUser(user);
            attendance.setAttendanceState(0);
            attendance.setLastWordId(lastWordId + 1);  // 학습 단어 넘어감
            attendance = attendanceRepository.save(attendance);

            // 2. TodayStudy 초기화
            TodayStudy todayStudy = new TodayStudy();
            todayStudy.setAttendance(attendance);
            todayStudy.setTrend(false);
            todayStudy.setWord(false);
            todayStudy.setArticle(false);
            todayStudyRepository.save(todayStudy);
        });

        System.out.println("정각에 Attendance와 TodayStudy가 초기화: " + todayDate);

    }
}