package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.Article.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertTranslationRepository extends JpaRepository<AlertTranslation, Integer> {
    List<AlertTranslation> findAlertTranslationByLanguage(Language language);
    AlertTranslation findByErrorTranslation(String errorTranslation);
    List<AlertTranslation> findByAlertCode(AlertCode alertCode);
}
