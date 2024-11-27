package salary_BE.salary.Domain;

import com.amazonaws.services.s3.event.S3EventNotification;
import jakarta.persistence.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;
import salary_BE.salary.Listener.UserEntityListener;

import java.util.List;

@Entity
@Getter @Setter
@EntityListeners(UserEntityListener.class) // 이벤트 리스너
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String nickname;

    private Integer age;

    private String gender;

    private String role;

    private Integer salaryPoint;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<WordLike> wordLikes;;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<UserBadgeMapping> userBadgeMappings;

}
