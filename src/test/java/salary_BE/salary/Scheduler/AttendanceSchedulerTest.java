package salary_BE.salary.Scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;

@SpringBootTest
@Transactional
public class AttendanceSchedulerTest {

    @Autowired
    private AttendanceScheduler attendanceScheduler;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInitializeAttendanceForNewUser() {
        User user = new User();
        user.setLoginId("TestLogin_" + System.currentTimeMillis());
        user.setUsername("TestUsername_" + System.currentTimeMillis());
        user.setPassword("securePassword");
        userRepository.save(user);

        attendanceScheduler.initializeAttendanceForNewUser(user);

        // 검증 로직 추가
    }
}