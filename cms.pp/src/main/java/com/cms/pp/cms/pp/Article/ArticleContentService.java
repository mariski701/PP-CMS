package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class ArticleContentService {
    @Autowired
    ArticleContentRepository articleContentRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    ArticleTagRepository articleTagRepository;



    public ArticleContent addArticleContent(ArticleContent articleContent) {
        return articleContentRepository.save(articleContent);
    }

    public ArticleContent getArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else
            return articleContent;
    }

    public int changeArticleStatus(int id, String articleStatus) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return HttpStatus.NOT_FOUND.value();
        else {
            articleContent.setPublished(articleStatus);
            articleContentRepository.save(articleContent);
            return HttpStatus.OK.value();
        }
    }

    public int removeArticle(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return HttpStatus.NOT_FOUND.value();
        else{
            articleContentRepository.deleteById(id);
            return HttpStatus.OK.value();
        }
    }

    public int editArticle(Integer id, String title, String language, Collection<Map<String, String>> tags, String content) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) return HttpStatus.NOT_FOUND.value();
        if(title.equals("") || language.equals("") || tags.isEmpty() || content.equals("")) {
            return HttpStatus.NOT_ACCEPTABLE.value();
        }
        else {
            if (!articleContent.getTitle().equals(title))
                articleContent.setTitle(title);
            if (!articleContent.getLanguage().getName().equals(language))
                articleContent.setLanguage(languageRepository.findByName(language));
            Collection<ArticleTag> articleTags  = new ArrayList<>();
            for (Map<String, String> names : tags) {
                articleTags.add(articleTagRepository.findByName(names.get("name")));
            }
            if (!articleContent.getArticleTags().equals(articleTags))
                articleContent.setArticleTags(articleTags);
            if (!articleContent.getContent().equals(content))
                articleContent.setContent(content);
            articleContentRepository.save(articleContent);
            return HttpStatus.OK.value();
        }
    }

    public List<ArticleContent> findAllByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (lang == null) return null;
        else {
            return articleContentRepository.findAllByLanguage(language);
        }

    }
}

/*
{
    "articleContents":  [
        {
            "content" : "jakis tekst po polsku",
            "title" : "jakis tytuł polski",
            "languages" : {
                "id" : 14
            }
        },
        {
            "content" : "some english stuff",
            "title" : "english is power",
            "languages" : {
                "id" : 13
            }
        }
    ],
    "articleTags" : [
        {
            "id" : 15
        }
    ]
}
 */