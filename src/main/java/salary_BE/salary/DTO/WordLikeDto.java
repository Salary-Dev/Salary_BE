package salary_BE.salary.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WordLikeDto {

    @JsonProperty("word_id")
    private Long wordId;

    @JsonProperty("word")
    private String word;

    @JsonProperty("like_date")
    private LocalDateTime likeDate;
}
