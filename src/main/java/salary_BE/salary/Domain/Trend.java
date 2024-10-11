package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Trend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trend_id")
    private Long id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private String incorrect1;

    @Column(nullable = false)
    private String incorrect2;

    @Column(nullable = false)
    private String incorrect3;

    @Column(nullable = false)
    private String trendQuiz;

    @Column(nullable = false)
    private String trendSolution;
}
