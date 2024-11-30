package salary_BE.salary.Integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.Domain.Attendance;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.AttendanceRepository;
import salary_BE.salary.Repository.UserRepository;
import salary_BE.salary.Service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    public void testUserCreationAndListenerIntegration() {
        User user = new User();
        user.setLoginId("TestLogin_" + System.currentTimeMillis());
        user.setUsername("TestUsername_" + System.currentTimeMillis());
        user.setPassword("securePassword");
        user.setNickname("testNickname");
        user.setAge(30);
        user.setGender("M");
        user.setRole("USER");
        user.setSalaryPoint(100);

        userService.createUser(user);

        Optional<User> savedUserOptional = userRepository.findByUsername(user.getUsername());
        assertThat(savedUserOptional).isPresent();

        User savedUser = savedUserOptional.get();
        Optional<Attendance> attendanceOptional = attendanceRepository.findTopByUserIdOrderByAttendanceDateDesc(savedUser.getId());
        assertThat(attendanceOptional).isPresent();
    }
}
