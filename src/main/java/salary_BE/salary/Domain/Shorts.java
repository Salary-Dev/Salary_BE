package salary_BE.salary.Domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
public class Shorts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shorts_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate shortsDate;

    @OneToMany(mappedBy = "shorts", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;
}