package salary_BE.salary.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setLoginId("ServiceTestLogin_" + System.currentTimeMillis());
        user.setUsername("ServiceTestUsername_" + System.currentTimeMillis());
        user.setPassword("securePassword");
        user.setNickname("testNickname");
        user.setAge(30);
        user.setGender("M");
        user.setRole("USER");
        user.setSalaryPoint(100);

        userService.createUser(user);

        Optional<User> savedUser = userRepository.findByUsername(user.getUsername());
        assertThat(savedUser).isPresent();
    }
}
