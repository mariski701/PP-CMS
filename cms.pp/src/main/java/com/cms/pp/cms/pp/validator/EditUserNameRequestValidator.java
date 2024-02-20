package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class EditUserNameRequestValidator {

	private final static String ANONYMOUS_USER = "anonymousUser";

	public Object validateEditUserName(String newUsername, String username) {
		if (newUsername == null || newUsername.isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3006.getValue());
		if (username == null || username.equals(ANONYMOUS_USER))
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
		return null;
	}

}
