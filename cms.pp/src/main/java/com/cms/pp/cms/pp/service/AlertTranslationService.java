package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.repository.LanguageRepository;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.repository.AlertCodeRepository;
import com.cms.pp.cms.pp.repository.AlertTranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service("AlertTranslationService")
public class AlertTranslationService {
    @Autowired
    private AlertTranslationRepository alertTranslationRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private AlertCodeRepository alertCodeRepository;

    public static AlertTranslationDTO createDTOTranslation(String alertCode, String alertName, String language, int id) {
        AlertTranslationDTO alertTranslationDTO = new AlertTranslationDTO();
        alertTranslationDTO.setId(id);
        alertTranslationDTO.setAlertName(alertName);
        alertTranslationDTO.setAlertCode(alertCode);
        alertTranslationDTO.setLanguage(language);
        return alertTranslationDTO;
    }

    public List<AlertTranslationDTO> findByLanguage(String language) {
        List<AlertTranslationDTO> alertTranslationDTOList = new ArrayList<>();
        if (language.equals("english"))
        {
            List<AlertCode> alertCodeList  = alertCodeRepository.findAll();
            for (AlertCode alertCode : alertCodeList) {
                alertTranslationDTOList.add(createDTOTranslation(alertCode.getAlertCode(), alertCode.getAlertName(), "english", alertCode.getId() ));
            }
            return alertTranslationDTOList;
        }
        Language lang = languageRepository.findByName(language);

        List<AlertTranslation> alertTranslationList = alertTranslationRepository.findAlertTranslationByLanguage(lang);
        for (AlertTranslation alertTranslation : alertTranslationList) {
            alertTranslationDTOList.add(createDTOTranslation(alertTranslation.getAlertCode().getAlertCode(), alertTranslation.getErrorTranslation(), language, alertTranslation.getId()));
        }

        return alertTranslationDTOList;
    }

    public Object addAlertTranslation(AlertTranslationDTO alertTranslationDTO) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        AlertTranslation alertTranslation = new AlertTranslation();
        alertTranslation.setLanguage(languageRepository.findByName(alertTranslationDTO.getLanguage()));
        alertTranslation.setAlertCode(alertCodeRepository.findByAlertCode(alertTranslationDTO.getAlertCode()));
        alertTranslation.setErrorTranslation(alertTranslationDTO.getAlertName());
        alertTranslationRepository.save(alertTranslation);
        return errorProvidedDataHandler;
    }

    public Object editAlertTranslation(int id, String errorTranslation) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        AlertTranslation alertTranslation = alertTranslationRepository.findById(id).orElse(null);
        if (alertTranslation == null) {
            errorProvidedDataHandler.setError(Code.CODE_3041.getValue());
            return errorProvidedDataHandler;
        }
        if (errorTranslation.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3043.getValue());
            return errorProvidedDataHandler;
        }
        alertTranslation.setErrorTranslation(errorTranslation);
        alertTranslationRepository.save(alertTranslation);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;

    }

    public AlertTranslation findById(int id) {
        return alertTranslationRepository.findById(id).orElse(null);
    }

}
