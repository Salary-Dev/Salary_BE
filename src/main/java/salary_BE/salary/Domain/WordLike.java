package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//유저_단어 매핑 테이블
@Entity
@Getter @Setter
public class WordLike {

    @Id @GeneratedValue
    @Column(name = "user_word_mapping_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(nullable = false)
    private boolean wordBookmark = false;

    private LocalDateTime likeDate;
}
