package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleContentRepository articleContentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleTagRepository articleTagRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Article getArticle(int id) {
        Article article =  articleRepository.findById(id).orElse(null);
        if (article == null)
            return null;
        else {
            long views = article.getViews();
            views++;
            article.setViews(views);
            articleRepository.save(article);
            return article;
        }
    }
    @Transactional
    public Article addArticle(Article article, int userId) {

        article.setUser(userRepository.findById(userId).orElse(null));

        articleRepository.save(article);
        List<ArticleContent> articleContents = (List<ArticleContent>)(article.getArticleContents());
        List<ArticleTag> articleTags = (List<ArticleTag>)(article.getArticleTags());


        for (int i = 0; i < articleContents.size(); i++) {
            articleContents.get(i).setArticle(article);
        }

        articleContentRepository.saveAll(articleContents);

        for (int i = 0; i < articleTags.size(); i++) {
            articleTags.get(i).setArticles(Arrays.asList(article));
        }

        return article;

    }

    //private ArticleDto convertArticleToArticleDto(Article article) {

   // }
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