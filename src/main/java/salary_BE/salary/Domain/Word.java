package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Word {

    @Id @GeneratedValue
    @Column (name = "word_id")
    private Long id;

    private String word;

    private String mean;

    private String story1;
    private String story2;
    private String story3;

    @OneToMany(mappedBy = "word")
    private List<WordLike> wordLikes;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleWordMapping> articleWordMappings;
}
