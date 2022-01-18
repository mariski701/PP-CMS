package com.cms.pp.cms.pp.Article;
import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {
    List<ArticleContent> findAllByLanguage(Language language);
    List<ArticleContent> findAllByLanguage(Language language, Sort sort);
    List<ArticleContent> findAllByUser(User user);
    List<ArticleContent> findAllByUser(User user, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
    ArticleContent findByTitle(String title);
    ArticleContent findArticleContentByComments(Comment comment);
    List<ArticleContent> findByArticleTags(ArticleTag articleTag);
    List<ArticleContent> findByArticleTags(ArticleTag articleTag, Sort sort);
}
