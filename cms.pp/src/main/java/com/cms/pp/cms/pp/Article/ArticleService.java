package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    @Autowired
    ArticleContentRepository articleContentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    /*public Article addArticle(Article article) {
        ArticleContent articleContent = new ArticleContent();

    }*/
}
