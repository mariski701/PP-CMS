package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleContentController {
    @Autowired
    private ArticleContentService articleContentService;

    @GetMapping("/api/articlecontent/{id}")
    public ArticleContent getArticleContent(@PathVariable int id) {
        return articleContentService.getArticleContent(id);
    }
}
