package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.dto.AlertTranslationDTO;
import com.cms.pp.cms.pp.model.entity.AlertCode;
import com.cms.pp.cms.pp.model.entity.AlertTranslation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@RequiredArgsConstructor
public class AlertTranslationMapper {

    public List<AlertTranslationDTO> mapToAlertTranslationDTOList(List<AlertCode> alertCodeList) {
        if (alertCodeList == null) {
            return new ArrayList<>();
        }
        List<AlertTranslationDTO> alertTranslationDTOList = new ArrayList<>();
        alertCodeList.forEach(alertCode -> alertTranslationDTOList.add(
                createAlertTranslationDTO(alertCode.getAlertCode(), alertCode.getAlertName(), "english", alertCode.getId()))
        );
        return alertTranslationDTOList;
    }

    public List<AlertTranslationDTO> mapToAlertTranslationDTOList(List<AlertTranslation> alertTranslationList, String language) {
        if (alertTranslationList == null) {
            return new ArrayList<>();
        }
        List<AlertTranslationDTO> alertTranslationDTOList = new ArrayList<>();
        alertTranslationList.forEach(alertTranslation -> alertTranslationDTOList.add(
                createAlertTranslationDTO(alertTranslation.getAlertCode().getAlertCode(), alertTranslation.getErrorTranslation(), language, alertTranslation.getId()))
        );
        return alertTranslationDTOList;
    }

    private AlertTranslationDTO createAlertTranslationDTO(String alertCode, String alertName, String language, int id) {
        return new AlertTranslationDTO()
                .setId(id)
                .setAlertName(alertName)
                .setAlertCode(alertCode)
                .setLanguage(language);
    }
}
