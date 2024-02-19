package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.RoleName;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Data
public class AddCMSUserRequestValidator {
    public Object validateAddCMSUser(CMSUserDTO cmsUserDTO) {
        if (cmsUserDTO.getRole() == null || !(Arrays.asList(RoleName.ROLE_ADMIN.getRoleName(),
                        RoleName.ROLE_EDITOR.getRoleName(),
                        RoleName.ROLE_MODERATOR.getRoleName())
                .contains(cmsUserDTO.getRole()))) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3020.getValue());
        }
        if (cmsUserDTO.getUserName() == null || cmsUserDTO.getUserName().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3023.getValue());
        if (cmsUserDTO.getUserPassword() == null || cmsUserDTO.getUserPassword().isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3024.getValue());
        return null;
    }
}
