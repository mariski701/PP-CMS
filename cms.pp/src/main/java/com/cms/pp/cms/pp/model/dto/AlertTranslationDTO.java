package com.cms.pp.cms.pp.model.dto;

import lombok.Data;

@Data
public class AlertTranslationDTO {
    private int id;
    private String alertCode;
    private String alertName;
    private String language;
}
