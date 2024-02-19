package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class EditLanguageRequestValidator {
    public Object validateEditLanguage(Language lang) {
        if (lang.getName() == null ||lang.getName().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3037.getValue());
        if (lang.getLanguageCode() == null || lang.getLanguageCode().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3038.getValue());
        return null;
    }
}
