package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;

import java.util.List;

public interface IAlertTranslationService {

	List<AlertTranslationDTO> findByLanguage(String language);

	Object addAlertTranslation(AlertTranslationDTO alertTranslationDTO);

	Object editAlertTranslation(int id, String errorTranslation);

	AlertTranslation findById(int id);

}
