package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.CustomCorsConfigAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/articles/")
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

    @GetMapping("/top/{language}/{count}")
    public List<ArticleContent> findSomeArticlesByViews(@PathVariable int count, @PathVariable String language) {
        return articleContentService.findSomeArticlesByViews(count, language);
    }

    @GetMapping("all")
    public List<ArticleContent> findAll() {
        return articleContentService.findAll();
    }

    /*@GetMapping("contains/{title}")
    public List<ArticleContent> findByTitleIgnoreCaseContaining(@PathVariable String title) {
        return articleContentService.findByTitleIgnoreCaseContaining(title);
    }*/

    @PostMapping(value = {"{language}/contains","{language}/contains/{title}"})
    public List<ArticleContent> findByTitleIgnoreCaseContainingOrByTags(@PathVariable String language, @PathVariable Map<String, String> title, @RequestBody List<Map<String, String>> tagNames) {
        //System.out.println(language);
        String tit = title.get("title");
        if (tit != null && !(tagNames.isEmpty()))
        {
            //System.out.println("ze stringiem i z tagami");
            return articleContentService.findByTitleIgnoreCaseContainingOrByTags(language, tit, tagNames);
        }
        else if (tit != null && tagNames.isEmpty()) {
            //System.out.println("z tytu??em ale bez tag??w");
            return articleContentService.findByTitleIgnoreCaseContainingOrByTags(language, tit, null);
        }
        else if (tit == null && !(tagNames.get(0).isEmpty())) {
           // System.out.println("z tagami bez tytulu");
            return articleContentService.findByTitleIgnoreCaseContainingOrByTags(language, "", tagNames);
        }
        return null;
    }
    /*


    je??eli nie ma tytu??u to wy??wietli artyku??y z wybranych tag??w
    localhost:8080/api/articles/contains/
    notacja JSONa:
    [
        {
            "name" : "og??lny"
        },
        {
            "name" : "kulinaria"
        }
    ]


    Je??eli jest tytu?? LIKE i s?? tagi to:
    localhost:8080/api/articles/contains/{co??}
    notacja JSONa:
    [
        {
            "name" : "og??lny"
        },
        {
            "name" : "kulinaria"
        }
    ]

    Je??eli nie ma tag??w a jest tytu?? to:
    localhost:8080/api/articles/contains/{co??}
    notacja JSONa:
    []
     */


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

    @GetMapping("findbytag/{tagName}")
    public List<ArticleContent> findByTagName(@PathVariable String tagName) {
        return articleContentService.findByTag(tagName);
    }

    @GetMapping("cms/findall")
    public List<ArticleContent> getAllForCMS() {
        return articleContentService.getAllForCMS();
    }

    @GetMapping("cms/findbyuser")
    public List<ArticleContent> getAllByUsersInCMS() {
        return articleContentService.getAllByUsersInCMS();
    }


}
