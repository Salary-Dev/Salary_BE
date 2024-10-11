package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private LocalDateTime date;

    //조회수
    @Column(nullable = false)
    private Long hits;

    private String source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shorts_id", nullable = false)
    private Shorts shorts;

}
