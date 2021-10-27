package com.cms.pp.cms.pp.Article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language findByName(String name);
}
