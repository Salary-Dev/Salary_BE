package salary_BE.salary.Listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Event.UserCreatedEvent;
import salary_BE.salary.Scheduler.AttendanceScheduler;

@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {

    private final AttendanceScheduler attendanceScheduler;

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        User user = event.getUser();
        System.out.println("UserCreatedEventListener 처리 중 - User ID: " + user.getId());
        attendanceScheduler.initializeAttendanceForNewUser(user);
    }
}