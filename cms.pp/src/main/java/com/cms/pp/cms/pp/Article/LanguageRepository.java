package com.cms.pp.cms.pp.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language findByName(String name);


}
