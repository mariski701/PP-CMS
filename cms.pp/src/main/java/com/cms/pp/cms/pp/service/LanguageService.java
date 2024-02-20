package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.validator.AddLanguageRequestValidator;
import com.cms.pp.cms.pp.validator.EditLanguageRequestValidator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class LanguageService implements ILanguageService {

	private final LanguageRepository languageRepository;

	private final ArticleContentRepository articleContentRepository;

	private final CommentRepository commentRepository;

	private final AlertTranslationRepository alertTranslationRepository;

	private final ArticleTagRepository articleTagRepository;

	private final AddLanguageRequestValidator addLanguageRequestValidator;

	private final EditLanguageRequestValidator editLanguageRequestValidator;

	@Override
	public Object addLanguage(Language language) {
		Object validateRequest = addLanguageRequestValidator.validateAddLanguage(language);
		if (validateRequest != null)
			return validateRequest;
		Language langTemp = languageRepository.findByName(language.getName());
		Language langTemp2 = languageRepository.findByLanguageCode(language.getLanguageCode());
		if (langTemp2 != null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
		if (langTemp != null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
		languageRepository.save(language);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public Object removeLanguage(int id) {
		Language language = languageRepository.findById(id).orElse(null);
		if (language == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3018.getValue());
		if (id <= 1)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3040.getValue());
		List<ArticleContent> articleContentList = articleContentRepository.findAllByLanguage(language);
		List<ArticleTag> articleTagList = articleTagRepository.findByLanguage(language);
		List<AlertTranslation> alertTranslationList = alertTranslationRepository
			.findAlertTranslationByLanguage(language);
		List<List<Comment>> commentList = new ArrayList<>();
		for (ArticleContent articleContent : articleContentList) {
			commentList.add(commentRepository.findByArticleContent(articleContent));
		}
		for (List<Comment> commentList1 : commentList) {
			commentRepository.deleteAll(commentList1);
		}
		articleContentRepository.deleteAll(articleContentList);
		articleTagRepository.deleteAll(articleTagList);
		alertTranslationRepository.deleteAll(alertTranslationList);
		languageRepository.deleteById(id);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());

	}

	@Override
	public List<Language> getAllLanguages() {
		return languageRepository.findAll();
	}

	@Override
	public Language getLanguage(String name) {
		return languageRepository.findByName(name);
	}

	@Override
	public Language getLanguageById(int id) {
		return languageRepository.findById(id).orElse(null);
	}

	@Override
	public Object editLanguage(Language lang) {
		Object requestValidator = editLanguageRequestValidator.validateEditLanguage(lang);
		if (requestValidator != null)
			return requestValidator;
		Language language = languageRepository.findById(lang.getId()).orElse(null);
		Language checkLangName = languageRepository.findByName(lang.getName());
		Language checkLangCode = languageRepository.findByLanguageCode(lang.getLanguageCode());
		if (language == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3018.getValue());
		if (checkLangCode != null || checkLangName != null) {
			if (!language.getName().equals(lang.getName())
					|| !language.getLanguageCode().equals(lang.getLanguageCode())) {
				return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3039.getValue());
			}
		}
		language.setName(lang.getName());
		language.setLanguageCode(lang.getLanguageCode());
		languageRepository.save(language);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

}
