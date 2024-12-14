package salary_BE.salary.DTO;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
=======
>>>>>>> 7245f7f (feat : 북마킹 단어 기능 구현)
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WordLikeDto {
<<<<<<< HEAD

    @JsonProperty("word_id")
    private Long wordId;

    @JsonProperty("word")
    private String word;

    @JsonProperty("like_date")
    private LocalDateTime likeDate;
=======
    private Long word_id;
    private String word;
    private LocalDateTime like_date;
>>>>>>> 7245f7f (feat : 북마킹 단어 기능 구현)
}
