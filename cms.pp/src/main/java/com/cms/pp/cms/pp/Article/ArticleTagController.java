package com.cms.pp.cms.pp.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleTagController {
    @Autowired
    ArticleTagService articleTagService;

    @GetMapping("/api/tags")
    public List<ArticleTag> getArticleTags () {
        return articleTagService.getArticleTags();
    }

    @GetMapping("/api/tag/{id}")
    public ArticleTag getArticleTag(@PathVariable int id) {
        return articleTagService.getArticleTag(id);
    }

    @PostMapping("/api/tag/add")
    public ArticleTag addTag (@RequestBody ArticleTag articleTag) {
        return articleTagService.addTag(articleTag);
    }

    @DeleteMapping("/api/tag/remove/{id}")
    public String removeTag(@PathVariable int id) {
        return articleTagService.removeTag(id);
    }

    @PutMapping("/api/tag/modify/{id}")
    public ArticleTag modifyTag(@RequestBody ArticleTag articleTag, @PathVariable int id) {
        return articleTagService.modifyTag(id, articleTag);
    }

    @GetMapping("/api/tag/language/{lang}")
    public List<ArticleTag> findByLanguage(@PathVariable  String lang) {
        return articleTagService.findByLanguage(lang);
    }
}
