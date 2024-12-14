package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EconomyLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "economy_letter_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "economy_letter_editor_id", nullable = false)
    private EconomyLetterEditor editor;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "text", nullable = true)
    private String text;

    @Column(name = "hits", nullable = true)
    private Integer hits;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성 시간
}
