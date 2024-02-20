package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Data
@RequiredArgsConstructor
public class AlertTranslationMapper {

	public List<AlertTranslationDTO> mapToAlertTranslationDTOList(List<AlertCode> alertCodeList) {
		return alertCodeList.stream()
			.map(alertCode -> createAlertTranslationDTO(alertCode.getAlertCode(), alertCode.getAlertName(), "english",
					alertCode.getId()))
			.collect(Collectors.toList());
	}

	public List<AlertTranslationDTO> mapToAlertTranslationDTOList(List<AlertTranslation> alertTranslationList,
			String language) {
		return alertTranslationList.stream()
			.map(alertTranslation -> createAlertTranslationDTO(alertTranslation.getAlertCode().getAlertCode(),
					alertTranslation.getErrorTranslation(), language, alertTranslation.getId()))
			.collect(Collectors.toList());
	}

	private AlertTranslationDTO createAlertTranslationDTO(String alertCode, String alertName, String language, int id) {
		return new AlertTranslationDTO().setId(id)
			.setAlertName(alertName)
			.setAlertCode(alertCode)
			.setLanguage(language);
	}

}
