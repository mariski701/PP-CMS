package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class EditAlertTranslationRequestValidator {
    public Object validateEditAlertTranslation(String errorTranslation) {
        if (errorTranslation == null ||errorTranslation.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3043.getValue());
        return null;
    }
}
