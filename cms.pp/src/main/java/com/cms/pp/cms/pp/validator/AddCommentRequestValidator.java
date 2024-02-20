package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.dto.CommentDTO;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class AddCommentRequestValidator {

	private static final String ANONYMOUS_USER = "anonymousUser";

	public Object validateAddComment(CommentDTO commentDTO, String username) {
		if (commentDTO.getContent() == null || commentDTO.getContent().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3004.getValue());
		if (username == null || username.equals(ANONYMOUS_USER))
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
		return null;
	}

}
