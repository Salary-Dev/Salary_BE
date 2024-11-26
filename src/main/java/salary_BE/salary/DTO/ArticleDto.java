package salary_BE.salary.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDto {
    private String url;
    private String title;
    private String image;
    private String source;
}
