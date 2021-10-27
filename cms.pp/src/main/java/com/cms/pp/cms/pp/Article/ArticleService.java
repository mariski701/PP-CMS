package com.cms.pp.cms.pp.Article;


import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleContentRepository articleContentRepository;

    @Autowired
    ArticleRepository articleRepository;


    @Autowired
    UserRepository userRepository;

    @Transactional
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    /*public Article addArticle(Article article) {
        ArticleContent articleContent = new ArticleContent();

    }*/
}
