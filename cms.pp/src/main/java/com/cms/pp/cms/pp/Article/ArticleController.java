package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /*@PostMapping(value = "/api/article/add/{userId}")
    public Article addArticle(@PathVariable int userId, @RequestBody Article article, @RequestBody List<ArticleContent> articleContents, @RequestBody List<ArticleTag> articleTags ) {
        return articleService.addArticle(article, articleContents, userId, articleTags);
    }*/

    @PostMapping(value = "/api/article/add/{userId}")
    public Article addArticle(@RequestBody Article article, @PathVariable int userId) {
        return articleService.addArticle(article, userId);
    }
}
