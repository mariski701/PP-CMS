package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {
    List<ArticleContent> findAllByLanguage(Language language);
    List<ArticleContent> findAllByLanguage(Language language, Sort sort);
    List<ArticleContent> findAllByLanguageAndPublished(Language language, String published, Sort sort);
    List<ArticleContent> findAllByLanguage(Language language, Pageable page);
    List<ArticleContent> findAllByLanguageAndPublished(Language language, String published, Pageable page);
    List<ArticleContent> findAllByUser(User user);
    List<ArticleContent> findAllByUser(User user, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContainingAndLanguage(String title, Language language, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContainingAndPublishedAndLanguage(String title, String published, Language language, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContainingAndArticleTags(String title, ArticleTag articleTag, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContainingAndArticleTagsAndLanguage( String title, ArticleTag articleTag, Language language, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContainingAndPublishedAndArticleTagsAndLanguage( String title, String published, ArticleTag articleTag, Language language, Sort sort);
    List<ArticleContent> findByTitleIgnoreCaseContaining(String title, Pageable pageable);
    ArticleContent findByTitle(String title);
    ArticleContent findArticleContentByComments(Comment comment);
    List<ArticleContent> findByArticleTags(ArticleTag articleTag);
    List<ArticleContent> findByArticleTagsAndLanguage(ArticleTag articleTag, Language language);
    List<ArticleContent> findByArticleTagsAndLanguage(ArticleTag articleTag, Language language, Sort sort);
    List<ArticleContent> findByArticleTags(ArticleTag articleTag, Sort sort);
    List<ArticleContent> findByPublishedAndArticleTags(String published, ArticleTag articleTag, Sort sort);
    List<ArticleContent> findByPublishedAndArticleTagsAndLanguage(String published, ArticleTag articleTag, Language language, Sort sort);
    List<ArticleContent> findDistinctByArticleTags(ArticleTag articleTag, Sort sort);

}
