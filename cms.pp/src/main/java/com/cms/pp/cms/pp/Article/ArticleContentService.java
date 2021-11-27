package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleContentService {
    @Autowired
    ArticleContentRepository articleContentRepository;



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