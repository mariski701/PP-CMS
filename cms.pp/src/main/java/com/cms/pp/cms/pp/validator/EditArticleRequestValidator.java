package com.cms.pp.cms.pp.validator;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Data
public class EditArticleRequestValidator {

	private final static String ANONYMOUS_USER = "anonymousUser";

	public Object validateEditArticle(ArticleContentDTO articleContentDTO, String username) {
		if (articleContentDTO.getTitle() == null || articleContentDTO.getTitle().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3001.getValue());
		if (articleContentDTO.getLanguage() == null || articleContentDTO.getLanguage().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3002.getValue());
		if (articleContentDTO.getTags() == null || articleContentDTO.getTags().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3003.getValue());
		if (articleContentDTO.getContent() == null || articleContentDTO.getContent().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3004.getValue());
		if (articleContentDTO.getImage() == null || articleContentDTO.getImage().isEmpty())
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3032.getValue());
		if (username == null || username.equals(ANONYMOUS_USER))
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
		return null;
	}

}
