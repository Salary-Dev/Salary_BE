package salary_BE.salary.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Editor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "economy_letter_editor_id")
    private Long id;

    @Column(name = "economy_letter_editor_name")
    private Long name;
}
