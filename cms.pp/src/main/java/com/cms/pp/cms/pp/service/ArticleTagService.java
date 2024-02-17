package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.repository.ArticleTagRepository;
import com.cms.pp.cms.pp.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    ArticleTagRepository articleTagRepository;
    @Autowired
    LanguageRepository languageRepository;

    public List<ArticleTag> getArticleTags ()  {
        return articleTagRepository.findAll();
    }
    public ArticleTag getArticleTag(int id) {
        return articleTagRepository.findById(id).orElse(null);
    }

    public Object addTag(String language, String name) {
        Language lang = languageRepository.findByName(language);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (lang == null) {
            errorProvidedDataHandler.setError(Code.CODE_3015.getValue());
            return errorProvidedDataHandler;
        }
        ArticleTag checkIfTagExists = articleTagRepository.findByName(name);
        if (checkIfTagExists == null) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setLanguage(lang);
            articleTag.setName(name);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            articleTagRepository.save(articleTag);
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError(Code.CODE_3014.getValue());
            return errorProvidedDataHandler;
        }

    }

    public Object removeTag(int id) {
        ArticleTag articleTag = articleTagRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleTag == null) {
            errorProvidedDataHandler.setError(Code.CODE_3016.getValue());
            return errorProvidedDataHandler;
        }
        articleTagRepository.delete(articleTag);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;

    }

    public Object modifyTag(int id, ArticleTag articleTag) {
        ArticleTag oldArticleTag = articleTagRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (oldArticleTag == null) {
            errorProvidedDataHandler.setError(Code.CODE_3016.getValue());
            return errorProvidedDataHandler;
        }
        ArticleTag articleTagTemp = articleTagRepository.findByName(articleTag.getName());
        if (articleTagTemp != null)
        {
            errorProvidedDataHandler.setError(Code.CODE_3014.getValue());
            return errorProvidedDataHandler;
        }
        oldArticleTag.setName(articleTag.getName());
        articleTagRepository.save(oldArticleTag);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;

    }

    public List<ArticleTag> findByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (language == null) 
            return null;
        return articleTagRepository.findByLanguage(language);
    }
}
