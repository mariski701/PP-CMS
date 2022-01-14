package com.cms.pp.cms.pp.Article;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/articles/")
@RestController
public class ArticleContentController {
    @Autowired
    private ArticleContentService articleContentService;

    @PostMapping("add")
    public Object addArticle(@RequestBody ArticleContentDTO articleContentDTO) {
        return articleContentService.addArticleContent(articleContentDTO);
    }

    @GetMapping("get/{id}")
    public ArticleContent getArticleContent(@PathVariable int id) {
        return articleContentService.getArticleContent(id);
    }

    @PutMapping("publish")
    public Object changeArticleStatus(@RequestBody Map<String, String> body) {
        return articleContentService.changeArticleStatus(Integer.parseInt(body.get("id")), body.get("status"));
    }

    @DeleteMapping("delete/{id}")
    public Object removeArticle(@PathVariable int id) {
        return articleContentService.removeArticle(id);
    }

    @PutMapping("edit")
    public Object editArticle(@RequestBody ArticleContentDTO articleContentDTO) {
        if(articleContentDTO.getId().equals(null))
            articleContentDTO.setId(0);
        return articleContentService.editArticle(articleContentDTO.getId(),
                articleContentDTO.getTitle(),
                articleContentDTO.getLanguage(),
                articleContentDTO.getTags(),
                articleContentDTO.getContent(),
                articleContentDTO.getImage());
    }

    @GetMapping("{language}")
    public List<ArticleContent> findAllByLanguage(@PathVariable String language) {
        return articleContentService.findAllByLanguage(language);
    }

    @GetMapping("user/{id}")
    public List<ArticleContent> findAllByUser(@PathVariable int id) {
        return articleContentService.findAllByUser(id);
    }

    @GetMapping("/top/{count}")
    public Page<ArticleContent> findSomeArticlesByViews(@PathVariable int count) {
        return articleContentService.findSomeArticlesByViews(count);
    }

    @GetMapping("all")
    public List<ArticleContent> findAll() {
        return articleContentService.findAll();
    }

    @GetMapping("contains/{title}")
    public List<ArticleContent> findByTitleIgnoreCaseContaining(@PathVariable String title) {
        return articleContentService.findByTitleIgnoreCaseContaining(title);
    }

    @GetMapping("findByTitle")
    public ArticleContent findByTitle(@RequestBody Map<String, String> title){
        return articleContentService.findByTitle(title.get("title"));
    }

    @GetMapping("find/{page}/{size}/{title}")
    public List<ArticleContentDTO> findSomeArticlesByLazyLoading(@PathVariable int page, @PathVariable int size, @PathVariable String title) {
        List<ArticleContent> articleContentList = articleContentService.findSomeArticlesByLazyLoading(page, size, title);
        List<ArticleContentDTO> articleContentDTOList = new ArrayList<>();

        for (int i = 0; i < articleContentList.size(); i++) {
            articleContentDTOList.add(new ArticleContentDTO());
            articleContentDTOList.get(i).setId(articleContentList.get(i).getId());
            articleContentDTOList.get(i).setTitle(articleContentList.get(i).getTitle());
            articleContentDTOList.get(i).setLanguage(articleContentList.get(i).getLanguage().getName());
        }
        return articleContentDTOList;
    }

    @PutMapping("{id}/allowcomments/{allowComments}")
    public Object allowCommentsInArticle(@PathVariable int id, @PathVariable boolean allowComments) {
        return articleContentService.allowCommentsInArticle(id, allowComments);
    }

    @GetMapping("findbycomment/{id}")
    public ArticleContent getArticleContentByCommentId(@PathVariable Long id) {
        return articleContentService.getArticleContentByCommentId(id);
    }

}
