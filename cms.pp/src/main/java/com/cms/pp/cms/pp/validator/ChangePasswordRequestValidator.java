package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class ChangePasswordRequestValidator {
    private final static String ANONYMOUS_USER = "anonymousUser";

    public Object validateChangePassword(String oldPassword, String newPassword, String username) {
        if (oldPassword == null || oldPassword.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3007.getValue());
        if (newPassword == null || newPassword.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3008.getValue());
        if (username == null || username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
        return null;
    }
}
