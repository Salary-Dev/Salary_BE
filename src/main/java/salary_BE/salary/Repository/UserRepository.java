package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salary_BE.salary.Domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}
