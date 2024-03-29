package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

	Language findByName(String name);

	Language findByLanguageCode(String languageCode);

}
