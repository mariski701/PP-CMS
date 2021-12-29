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

    public static AlertTranslationDTO createDTOTranslation(int alertCode, String alertName, String langauge, int id) {
        AlertTranslationDTO alertTranslationDTO = new AlertTranslationDTO();
        alertTranslationDTO.setId(id);
        alertTranslationDTO.setAlertName(alertName);
        alertTranslationDTO.setAlertCode(alertCode);
        alertTranslationDTO.setLanguage(langauge);
        return alertTranslationDTO;
    }

    public List<?> findByLanguage(String language) {
        if (language.equals("english"))
        {
            return alertCodeRepository.findAll();
        }
        Language lang = languageRepository.findByName(language);
        List<AlertTranslationDTO> alertTranslationDTOList = new ArrayList<>();
        List<AlertTranslation> alertTranslationList = alertTranslationRepository.findAlertTranslationByLanguage(lang);
        for (AlertTranslation alertTranslation : alertTranslationList) {
            alertTranslationDTOList.add(createDTOTranslation(alertTranslation.getAlertCode().getAlertCode(), alertTranslation.getErrorTranslation(), language, alertTranslation.getId()));
        }

        return alertTranslationDTOList;/*alertTranslationRepository.findAlertTranslationByLanguage(lang);*/
    }

    public int addAlertTranslation(AlertTranslationDTO alertTranslationDTO) {
        AlertTranslation alertTranslation = new AlertTranslation();
        alertTranslation.setLanguage(languageRepository.findByName(alertTranslationDTO.getLanguage()));
        alertTranslation.setAlertCode(alertCodeRepository.findByAlertCode(alertTranslationDTO.getAlertCode()));
        alertTranslation.setErrorTranslation(alertTranslationDTO.getAlertName());
        alertTranslationRepository.save(alertTranslation);
        return 2001;
    }

}
