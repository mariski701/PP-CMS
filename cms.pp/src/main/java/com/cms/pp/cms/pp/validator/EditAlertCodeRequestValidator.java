package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
public class EditAlertCodeRequestValidator {
    public Object validateEditAlertCode(String alertCode, String alertName) {
        if (alertCode == null || alertCode.isEmpty()) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3042.getValue());
        }
        if (alertName == null || alertName.isEmpty()) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3043.getValue());
        }
        return null;
    }
}
