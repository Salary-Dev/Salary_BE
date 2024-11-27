package salary_BE.salary.Listener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Scheduler.AttendanceScheduler;

@SpringBootTest
@Transactional
public class UserEntityListenerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAfterUserCreated() {
        User user = new User();
        user.setLoginId("ListenerTestLogin_" + System.currentTimeMillis());
        user.setUsername("ListenerTestUsername_" + System.currentTimeMillis());
        user.setPassword("securePassword");
        user.setNickname("testNickname");
        user.setAge(30);
        user.setGender("M");
        user.setRole("USER");
        user.setSalaryPoint(100);

        userRepository.save(user); // 엔티티 리스너 트리거

        // 검증 로직 추가 (예: 관련 Attendance 데이터 확인)
    }
}
