package com.cms.pp.cms.pp.Alerts;

import com.cms.pp.cms.pp.Article.Language;
import com.cms.pp.cms.pp.Article.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
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

    public String addAlertTranslation(AlertTranslationDTO alertTranslationDTO) {
        AlertTranslation alertTranslation = new AlertTranslation();
        alertTranslation.setLanguage(languageRepository.findByName(alertTranslationDTO.getLanguage()));
        alertTranslation.setAlertCode(alertCodeRepository.findByAlertCode(alertTranslationDTO.getAlertCode()));
        alertTranslation.setErrorTranslation(alertTranslationDTO.getAlertName());
        alertTranslationRepository.save(alertTranslation);
        return "message.2001";
    }

}
