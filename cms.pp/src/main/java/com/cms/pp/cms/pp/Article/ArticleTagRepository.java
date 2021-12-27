package com.cms.pp.cms.pp.Article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Integer> {
    ArticleTag findByName(String name);
    List<ArticleTag> findByLanguage(Language language);
}
