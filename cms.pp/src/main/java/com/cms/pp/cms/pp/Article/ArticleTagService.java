package com.cms.pp.cms.pp.Article;

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

    public ArticleTag addTag(ArticleTag articleTag) {
        return articleTagRepository.save(articleTag);
    }

    public String removeTag(int id) {
        ArticleTag articleTag = articleTagRepository.findById(id).orElse(null);
        if (articleTag == null) return "Error";
        else {
            articleTagRepository.delete(articleTag);
            return "Tag of ID: " + articleTag.getId() +" and name "+articleTag.getName()+" is deleted";
        }
    }

    public ArticleTag modifyTag(int id, ArticleTag articleTag) {
        ArticleTag oldArticleTag = articleTagRepository.findById(id).orElse(null);
        if (oldArticleTag == null) return null;
        else {
            oldArticleTag.setName(articleTag.getName());
            return articleTagRepository.save(oldArticleTag);
        }
    }

    public List<ArticleTag> findByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (language == null) return null;
        else {
            return articleTagRepository.findByLanguage(language);
        }
    }
}
