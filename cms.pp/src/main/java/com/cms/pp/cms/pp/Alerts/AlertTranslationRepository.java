package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.Article.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertTranslationRepository extends JpaRepository<AlertTranslation, Integer> {
    List<AlertTranslation> findAlertTranslationByLanguage(Language language);
}
