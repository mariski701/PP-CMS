package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import com.cms.pp.cms.pp.service.IArticleContentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/articles/")
public class ArticleContentController {
    private final IArticleContentService IArticleContentService;

    @PostMapping("add")
    public Object addArticle(@RequestBody ArticleContentDTO articleContentDTO) {
        return IArticleContentService.addArticleContent(articleContentDTO);
    }

    @GetMapping("get/{id}")
    public ArticleContent getArticleContent(@PathVariable int id) {
        return IArticleContentService.getArticleContent(id);
    }

    @PutMapping("publish")
    public Object changeArticleStatus(@RequestBody Map<String, String> body) {
        return IArticleContentService.changeArticleStatus(Integer.parseInt(body.get("id")), body.get("status"));
    }

    @DeleteMapping("delete/{id}")
    public Object removeArticle(@PathVariable int id) {
        return IArticleContentService.removeArticle(id);
    }

    @PutMapping("edit")
    public Object editArticle(@RequestBody ArticleContentDTO articleContentDTO) {
        if(articleContentDTO.getId().equals(null))
            articleContentDTO.setId(0);
        return IArticleContentService.editArticle(articleContentDTO.getId(),
                articleContentDTO.getTitle(),
                articleContentDTO.getLanguage(),
                articleContentDTO.getTags(),
                articleContentDTO.getContent(),
                articleContentDTO.getImage());
    }

    @GetMapping("{language}")
    public List<ArticleContent> findAllByLanguage(@PathVariable String language) {
        return IArticleContentService.findAllByLanguage(language);
    }

    @GetMapping("user/{id}")
    public List<ArticleContent> findAllByUser(@PathVariable int id) {
        return IArticleContentService.findAllByUser(id);
    }

    @GetMapping("/top/{language}/{count}")
    public List<ArticleContent> findSomeArticlesByViews(@PathVariable int count, @PathVariable String language) {
        return IArticleContentService.findSomeArticlesByViews(count, language);
    }

    @GetMapping("all")
    public List<ArticleContent> findAll() {
        return IArticleContentService.findAll();
    }

    @PostMapping(value = {"{language}/contains","{language}/contains/{title}"})
    public List<ArticleContent> findByTitleIgnoreCaseContainingOrByTags(@PathVariable String language, @PathVariable Map<String, String> title, @RequestBody List<Map<String, String>> tagNames) {
        //System.out.println(language);
        String tit = title.get("title");
        if (tit != null && !(tagNames.isEmpty()))
        {
            //System.out.println("ze stringiem i z tagami");
            return IArticleContentService.findByTitleIgnoreCaseContainingOrByTags(language, tit, tagNames);
        }
        else if (tit != null && tagNames.isEmpty()) {
            //System.out.println("z tytułem ale bez tagów");
            return IArticleContentService.findByTitleIgnoreCaseContainingOrByTags(language, tit, null);
        }
        else if (tit == null && !(tagNames.get(0).isEmpty())) {
           // System.out.println("z tagami bez tytulu");
            return IArticleContentService.findByTitleIgnoreCaseContainingOrByTags(language, "", tagNames);
        }
        return null;
    }

    @GetMapping("findByTitle")
    public ArticleContent findByTitle(@RequestBody Map<String, String> title){
        return IArticleContentService.findByTitle(title.get("title"));
    }

    @GetMapping("find/{page}/{size}/{title}")
    public List<ArticleContentDTO> findSomeArticlesByLazyLoading(@PathVariable int page, @PathVariable int size, @PathVariable String title) {
        List<ArticleContent> articleContentList = IArticleContentService.findSomeArticlesByLazyLoading(page, size, title);
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
        return IArticleContentService.allowCommentsInArticle(id, allowComments);
    }

    @GetMapping("findbycomment/{id}")
    public ArticleContent getArticleContentByCommentId(@PathVariable Long id) {
        return IArticleContentService.getArticleContentByCommentId(id);
    }

    @GetMapping("findbytag/{tagName}")
    public List<ArticleContent> findByTagName(@PathVariable String tagName) {
        return IArticleContentService.findByTag(tagName);
    }

    @GetMapping("cms/findall")
    public List<ArticleContent> getAllForCMS() {
        return IArticleContentService.getAllForCMS();
    }

    @GetMapping("cms/findbyuser")
    public List<ArticleContent> getAllByUsersInCMS() {
        return IArticleContentService.getAllByUsersInCMS();
    }


}
