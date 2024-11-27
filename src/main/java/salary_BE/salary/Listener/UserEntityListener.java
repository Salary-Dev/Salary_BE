package salary_BE.salary.Listener;

import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Scheduler.AttendanceScheduler;
import salary_BE.salary.Service.AttendanceService;
import salary_BE.salary.Util.SpringContext;

@Component
public class UserEntityListener {

    @PostPersist
    public void afterUserCreated(User user) {
        AttendanceScheduler attendanceScheduler = SpringContext.getBean(AttendanceScheduler.class);
        attendanceScheduler.initializeAttendanceForNewUser(user);
    }
}
