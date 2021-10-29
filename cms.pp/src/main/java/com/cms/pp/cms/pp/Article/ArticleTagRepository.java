package com.cms.pp.cms.pp.Article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Integer> {
    ArticleTag findByName(String name);
}
