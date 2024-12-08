package salary_BE.salary.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class WordmainDto {
    private Long word_id;
    private String word;
    private String mean;
    private String story1;
    private String story2;
    private String story3;
    private String example;
    private List<ArticleDto> articles;
    private Boolean isSaved; // 단어장 저장 여부
}
