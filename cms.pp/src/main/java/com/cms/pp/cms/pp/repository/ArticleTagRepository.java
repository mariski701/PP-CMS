package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Integer> {
    ArticleTag findByName(String name);
    List<ArticleTag> findByLanguage(Language language);
}
