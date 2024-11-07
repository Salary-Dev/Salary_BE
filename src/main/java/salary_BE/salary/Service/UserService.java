package salary_BE.salary.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {  // 현재 mock 데이터 반환
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Mock user not found")); // mock 데이터 없어진 경우 예외 처리
    }
}
