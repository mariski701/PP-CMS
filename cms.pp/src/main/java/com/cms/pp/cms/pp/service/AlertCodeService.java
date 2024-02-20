package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import com.cms.pp.cms.pp.repository.AlertCodeRepository;
import com.cms.pp.cms.pp.repository.AlertTranslationRepository;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.validator.AddAlertCodeRequestValidator;
import com.cms.pp.cms.pp.validator.EditAlertCodeRequestValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service("AlertCodeService")
public class AlertCodeService implements IAlertCodeService {

	private final AlertCodeRepository alertCodeRepository;

	private final AlertTranslationRepository alertTranslationRepository;

	private final AddAlertCodeRequestValidator addAlertCodeRequestValidator;

	private final EditAlertCodeRequestValidator editAlertCodeRequestValidator;

	@Override
	public List<AlertCode> getAlertCodes() {
		return alertCodeRepository.findAll();
	}

	@Override
	public Object removeAlertCode(int id) {
		AlertCode alertCode = alertCodeRepository.findById(id).orElse(null);
		if (alertCode == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3041.getValue());
		List<AlertTranslation> alertTranslationList = alertTranslationRepository.findByAlertCode(alertCode);
		if (!alertTranslationList.isEmpty()) {
			alertTranslationRepository.deleteAll(alertTranslationList);
		}
		alertCodeRepository.delete(alertCode);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public Object addAlertCode(String alertCode, String alertName) {
		Object validateRequest = addAlertCodeRequestValidator.validateAddAlertCode(alertCode, alertName);
		if (validateRequest != null)
			return validateRequest;
		AlertCode alertCodeTemp = alertCodeRepository.findByAlertCode(alertCode);
		if (alertCodeTemp != null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3044.getValue());
		alertCodeRepository.save(new AlertCode().setAlertCode(alertCode).setAlertName(alertName));
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public Object editAlertCode(int id, String alertCode, String alertName) {
		Object validateRequest = editAlertCodeRequestValidator.validateEditAlertCode(alertCode, alertName);
		if (validateRequest != null)
			return validateRequest;
		AlertCode alertCodeTemp = alertCodeRepository.findByAlertCode(alertCode);
		AlertCode editedAlertCode = alertCodeRepository.findById(id).orElse(null);
		if (alertCodeTemp != null && editedAlertCode != null)
			if (!editedAlertCode.getAlertCode().equals(alertCode))
				return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3044.getValue());
		if (editedAlertCode == null) {
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3041.getValue());
		}
		editedAlertCode.setAlertCode(alertCode);
		editedAlertCode.setAlertName(alertName);
		alertCodeRepository.save(editedAlertCode);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public AlertCode findById(int id) {
		return alertCodeRepository.findById(id).orElse(null);
	}

}
