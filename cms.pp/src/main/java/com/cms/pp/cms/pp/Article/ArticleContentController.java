package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RequestMapping("/api/article/")
@RestController
public class ArticleContentController {
    @Autowired
    private ArticleContentService articleContentService;

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
        System.out.println(articleContentDTO.toString());
        return articleContentService.editArticle(articleContentDTO.getId(),
                articleContentDTO.getTitle(),
                articleContentDTO.getLanguage(),
                articleContentDTO.getTags(),
                articleContentDTO.getContent());
    }
   // public int editArticle(int id, String title, String language, Collection<String> tags, String content)


}
