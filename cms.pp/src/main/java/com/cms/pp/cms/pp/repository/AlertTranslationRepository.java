package com.cms.pp.cms.pp.repository;

import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import com.cms.pp.cms.pp.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertTranslationRepository extends JpaRepository<AlertTranslation, Integer> {
    List<AlertTranslation> findAlertTranslationByLanguage(Language language);
    AlertTranslation findByErrorTranslation(String errorTranslation);
    List<AlertTranslation> findByAlertCode(AlertCode alertCode);
}
