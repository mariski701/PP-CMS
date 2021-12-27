package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/articles/")
@RestController
public class ArticleContentController {
    @Autowired
    private ArticleContentService articleContentService;

    @PostMapping("add")
    public int addArticle(@RequestBody ArticleContentDTO articleContentDTO) {
        return articleContentService.addArticleContent(articleContentDTO);
    }

    @GetMapping("get/{id}")
    public ArticleContent getArticleContent(@PathVariable int id) {
        return articleContentService.getArticleContent(id);
    }

    @PutMapping("publish")
    public int changeArticleStatus(@RequestBody Map<String, String> body) {
        return articleContentService.changeArticleStatus(Integer.parseInt(body.get("id")), body.get("status"));
    }

    @DeleteMapping("delete/{id}")
    public int removeArticle(@PathVariable int id) {
        return articleContentService.removeArticle(id);
    }

    @PutMapping("edit")
    public int editArticle(@RequestBody ArticleContentDTO articleContentDTO)
    {
        if(articleContentDTO.getId().equals(null))
            articleContentDTO.setId(0);
        return articleContentService.editArticle(articleContentDTO.getId(),
                articleContentDTO.getTitle(),
                articleContentDTO.getLanguage(),
                articleContentDTO.getTags(),
                articleContentDTO.getContent());
    }

    @GetMapping("{language}")
    public List<ArticleContent> findAllByLanguage(@PathVariable String language) {
        return articleContentService.findAllByLanguage(language);
    }

    @GetMapping("user/{id}")
    public List<ArticleContent> findAllByUser(@PathVariable int id) {
        return articleContentService.findAllByUser(id);
    }

}
