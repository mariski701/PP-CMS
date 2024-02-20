package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class ChangeUserMailRequestValidator {

	public Object validateChangeUserMail(String newMail) {
		if (newMail == null || newMail.isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3012.getValue());
		return null;
	}

}
