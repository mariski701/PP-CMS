package com.cms.pp.cms.pp.Article;
import com.cms.pp.cms.pp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {
    List<ArticleContent> findAllByLanguage(Language language);
    List<ArticleContent> findAllByUser(User user);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title);
    ArticleContent findByTitle(String title);
}
