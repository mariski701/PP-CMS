package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleContentService {
    @Autowired
    ArticleContentRepository articleContentRepository;

    @Autowired
    ArticleRepository articleRepository;

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

}
