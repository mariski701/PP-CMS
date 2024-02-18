package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.mapper.AlertTranslationMapper;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.repository.LanguageRepository;
import com.cms.pp.cms.pp.repository.AlertCodeRepository;
import com.cms.pp.cms.pp.repository.AlertTranslationRepository;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service("AlertTranslationService")
public class AlertTranslationService implements IAlertTranslationService {
    private final AlertTranslationRepository alertTranslationRepository;
    private final LanguageRepository languageRepository;
    private final AlertCodeRepository alertCodeRepository;
    private final AlertTranslationMapper alertTranslationMapper;

    @Override
    public List<AlertTranslationDTO> findByLanguage(String language) {
        if (language.equals("english"))
            return alertTranslationMapper.mapToAlertTranslationDTOList(alertCodeRepository.findAll());
        return alertTranslationMapper.mapToAlertTranslationDTOList(alertTranslationRepository.findAlertTranslationByLanguage(
                        languageRepository.findByName(language)),
                language);
    }

    @Override
    public Object addAlertTranslation(AlertTranslationDTO alertTranslationDTO) {
        alertTranslationRepository.save(new AlertTranslation()
                .setLanguage(languageRepository.findByName(alertTranslationDTO.getLanguage()))
                .setAlertCode(alertCodeRepository.findByAlertCode(alertTranslationDTO.getAlertCode()))
                .setErrorTranslation(alertTranslationDTO.getAlertName()));
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public Object editAlertTranslation(int id, String errorTranslation) {
        AlertTranslation alertTranslation = alertTranslationRepository.findById(id).orElse(null);
        if (alertTranslation == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3041.getValue());
        if (errorTranslation.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3043.getValue());
        alertTranslation.setErrorTranslation(errorTranslation);
        alertTranslationRepository.save(alertTranslation);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());

    }

    @Override
    public AlertTranslation findById(int id) {
        return alertTranslationRepository.findById(id).orElse(null);
    }

}
