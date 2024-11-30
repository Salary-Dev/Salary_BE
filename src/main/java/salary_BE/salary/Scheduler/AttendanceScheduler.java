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
import salary_BE.salary.Domain.User;
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

            // 마지막 Attendnace 기준
            Attendance lastAttendance = attendanceRepository
                    .findTopByUserIdOrderByAttendanceDateDesc(user.getId())
                    .orElse(null);

            // 학습할 단어 변경
            Long lastWordId = (lastAttendance != null) ? lastAttendance.getLastWordId() : 0L;

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

    // 유저 추가 시 학습률 및 오늘 학습 테이블 초기화
    @Transactional
    public void initializeAttendanceForNewUser(User user) {

        LocalDate todayDate = LocalDate.now(ZoneId.of("Asia/Seoul"));

        // Attendance 초기화
        Attendance attendance = new Attendance();
        attendance.setAttendanceDate(todayDate);
        attendance.setUser(user);
        attendance.setAttendanceState(0);
        attendance.setLastWordId(1L);  // 초기 단어 ID
        Attendance savedAttendance = attendanceRepository.save(attendance);

        // TodayStudy 초기화
        TodayStudy todayStudy = new TodayStudy();
        todayStudy.setAttendance(savedAttendance);
        todayStudy.setTrend(false);
        todayStudy.setWord(false);
        todayStudy.setArticle(false);
        todayStudyRepository.save(todayStudy);

        System.out.println("새로운 User에 대한 Attendance와 TodayStudy 초기화 완료: " + user.getId());
    }
}