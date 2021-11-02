package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping("/api/articles")
    public List<Article> getAllArticles() {
        return articleService.getArticles();
    }

    @GetMapping("/api/article/{id}")
    public Article getArticle(@PathVariable int id){
        return articleService.getArticle(id);
    }
}
