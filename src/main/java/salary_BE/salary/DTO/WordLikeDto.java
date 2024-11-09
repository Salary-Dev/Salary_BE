package salary_BE.salary.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WordLikeDto {
    private Long word_id;
    private String word;
    private LocalDateTime like_date;
}
