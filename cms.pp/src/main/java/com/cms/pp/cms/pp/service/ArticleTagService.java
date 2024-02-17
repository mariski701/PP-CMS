package com.cms.pp.cms.pp.service;

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
            errorProvidedDataHandler.setError("3015");
            return errorProvidedDataHandler;
        }
        ArticleTag checkIfTagExists = articleTagRepository.findByName(name);
        if (checkIfTagExists == null) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setLanguage(lang);

            articleTag.setName(name);
            errorProvidedDataHandler.setError("2001");
            articleTagRepository.save(articleTag);
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError("3014");
            return errorProvidedDataHandler;
        }

    }

    public Object removeTag(int id) {
        ArticleTag articleTag = articleTagRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleTag == null) {
            errorProvidedDataHandler.setError("3016");
            return errorProvidedDataHandler;
        }
        articleTagRepository.delete(articleTag);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;

    }

    public Object modifyTag(int id, ArticleTag articleTag) {
        ArticleTag oldArticleTag = articleTagRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (oldArticleTag == null) {
            errorProvidedDataHandler.setError("3016");
            return errorProvidedDataHandler;
        }
        ArticleTag articleTagTemp = articleTagRepository.findByName(articleTag.getName());
        if (articleTagTemp != null)
        {
            errorProvidedDataHandler.setError("3014");
            return errorProvidedDataHandler;
        }
        oldArticleTag.setName(articleTag.getName());
        errorProvidedDataHandler.setError("2001");
        articleTagRepository.save(oldArticleTag);
        return errorProvidedDataHandler;

    }

    public List<ArticleTag> findByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (language == null) return null;
        else {
            return articleTagRepository.findByLanguage(language);
        }
    }
}
