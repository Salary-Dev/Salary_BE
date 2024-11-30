package salary_BE.salary.Service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import salary_BE.salary.DTO.JoinDto;
import salary_BE.salary.Domain.User;
import salary_BE.salary.Exception.UsernameAlreadyExistsException;
import salary_BE.salary.Repository.UserRepository;

import java.util.List;

import static com.amazonaws.services.ec2.model.LaunchTemplateHttpTokensState.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDTO) {
        String loginId = joinDTO.getLoginId();

        // 아이디 중복 체크
        if (userRepository.existsByLoginId(loginId)) {
            throw new UsernameAlreadyExistsException("이미 존재하는 아이디입니다.");

        }

        User userData = new User();
        userData.setUsername(joinDTO.getUsername());
        userData.setLoginId(joinDTO.getLoginId());
        userData.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        userData.setRole("ROLE_USER");
        userData.setAge(joinDTO.getAge());
        userData.setGender(joinDTO.getGender());
        userData.setNickname(joinDTO.getNickname());
        userData.setSalaryPoint(0);

        userRepository.save(userData);
        System.out.println("유저 추가 완료");
    }
//    public User getCurrentUser() {  // 현재 mock 데이터 반환
//        return userRepository.findById(1L)
//                .orElseThrow(() -> new RuntimeException("Mock user not found")); // mock 데이터 없어진 경우 예외 처리
//    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); // 모든 유저 반환
    }

    // 새로운 사용자 생성
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
