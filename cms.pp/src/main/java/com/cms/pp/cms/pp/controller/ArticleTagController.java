package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.service.IArticleTagService;
import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
public class ArticleTagController {
    private final IArticleTagService IArticleTagService;

    @GetMapping("/api/tags")
    public List<ArticleTag> getArticleTags () {
        return IArticleTagService.getArticleTags();
    }

    @GetMapping("/api/tag/{id}")
    public ArticleTag getArticleTag(@PathVariable int id) {
        return IArticleTagService.getArticleTag(id);
    }

    @PostMapping("/api/tag/add")
    public Object addTag (@RequestBody Map<String, String> body) {
        return IArticleTagService.addTag(body.get("language"), body.get("name"));
    }

    @DeleteMapping("/api/tag/remove/{id}")
    public Object removeTag(@PathVariable int id) {
        return IArticleTagService.removeTag(id);
    }

    @PutMapping("/api/tag/modify/{id}")
    public Object modifyTag(@RequestBody ArticleTag articleTag, @PathVariable int id) {
        return IArticleTagService.modifyTag(id, articleTag);
    }

    @GetMapping("/api/tag/language/{lang}")
    public List<ArticleTag> findByLanguage(@PathVariable  String lang) {
        return IArticleTagService.findByLanguage(lang);
    }
}
