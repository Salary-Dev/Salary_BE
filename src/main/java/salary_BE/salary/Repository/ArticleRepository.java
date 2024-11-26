package salary_BE.salary.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import salary_BE.salary.Domain.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = "SELECT * FROM articles ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Article> findRandomArticles(@Param("count") int count);  // 기사 랜덤으로 10개 추출
}
