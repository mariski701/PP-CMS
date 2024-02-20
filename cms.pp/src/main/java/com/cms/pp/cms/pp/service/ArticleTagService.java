package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.repository.ArticleTagRepository;
import com.cms.pp.cms.pp.repository.LanguageRepository;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service("ArticleTagService")
public class ArticleTagService implements IArticleTagService {

	private final ArticleTagRepository articleTagRepository;

	private final LanguageRepository languageRepository;

	@Override
	public List<ArticleTag> getArticleTags() {
		return articleTagRepository.findAll();
	}

	@Override
	public ArticleTag getArticleTag(int id) {
		return articleTagRepository.findById(id).orElse(null);
	}

	@Override
	public Object addTag(String language, String name) {
		Language lang = languageRepository.findByName(language);
		if (lang == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3015.getValue());
		ArticleTag checkIfTagExists = articleTagRepository.findByName(name);
		if (checkIfTagExists == null) {
			articleTagRepository.save(new ArticleTag().setLanguage(lang).setName(name));
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
		}
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3014.getValue());
	}

	@Override
	public Object removeTag(int id) {
		ArticleTag articleTag = articleTagRepository.findById(id).orElse(null);
		if (articleTag == null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3016.getValue());
		articleTagRepository.delete(articleTag);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public Object modifyTag(int id, ArticleTag articleTag) {
		ArticleTag oldArticleTag = articleTagRepository.findById(id).orElse(null);
		if (oldArticleTag == null) {
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3016.getValue());
		}
		ArticleTag articleTagTemp = articleTagRepository.findByName(articleTag.getName());
		if (articleTagTemp != null)
			return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3014.getValue());
		oldArticleTag.setName(articleTag.getName());
		articleTagRepository.save(oldArticleTag);
		return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
	}

	@Override
	public List<ArticleTag> findByLanguage(String lang) {
		Language language = languageRepository.findByName(lang);
		if (language == null)
			return null;
		return articleTagRepository.findByLanguage(language);
	}

}
