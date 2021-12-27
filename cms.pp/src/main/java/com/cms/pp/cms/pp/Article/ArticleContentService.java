package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class ArticleContentService {
    @Autowired
    ArticleContentRepository articleContentRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    ArticleTagRepository articleTagRepository;
    @Autowired
    UserRepository userRepository;



    public int addArticleContent(ArticleContentDTO articleContentDTO) {
        ArticleContent articleContent = new ArticleContent();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals("anonymousUser")) {
            return 3005; //user not logged in
        }
        else {
            if (articleContentDTO.getTitle().equals("")) {
                return 3001; //title empty
            }
            if (articleContentDTO.getLanguage().equals("")) {
                return 3002; //lang empty
            }
            if (articleContentDTO.getTags().isEmpty()) {
                return 3003; //tags empty
            }
            if (articleContentDTO.getContent().equals("")) {
                return 3004; //content empty
            }

            articleContent.setTitle(articleContentDTO.getTitle());
            Collection<ArticleTag> articleTags  = new ArrayList<>();
            for (Map<String, String> names : articleContentDTO.getTags()) {
                articleTags.add(articleTagRepository.findByName(names.get("name")));
            }
            articleContent.setArticleTags(articleTags);
            articleContent.setContent(articleContentDTO.getContent());
            Language language = languageRepository.findByName(articleContentDTO.getLanguage());
            articleContent.setLanguage(language);
            articleContent.setPublished("UNPUBLISHED");
            User user = userRepository.findByUserName(username);
            articleContent.setUser(user);
            articleContentRepository.save(articleContent);
            return 2001; //success
        }

    }

    public ArticleContent getArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else
            return articleContent;
    }

    public int changeArticleStatus(int id, String articleStatus) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return HttpStatus.NOT_FOUND.value();
        else {
            articleContent.setPublished(articleStatus);
            articleContentRepository.save(articleContent);
            return 2001; //successfully
        }
    }

    public int removeArticle(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return HttpStatus.NOT_FOUND.value();
        else{
            articleContentRepository.deleteById(id);
            return 2001;// successfully
        }
    }

    public int editArticle(Integer id, String title, String language, Collection<Map<String, String>> tags, String content) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) return HttpStatus.NOT_FOUND.value();
        if (title.equals("")) return 3001; //title empty
        if (language.equals("")) return 3002; //language empty
        if (tags.isEmpty()) return 3003; //tags empty <= than 0 tags
        if (content.equals("")) return 3004; //content empty
        else {
            articleContent.setTitle(title);
            articleContent.setLanguage(languageRepository.findByName(language));
            Collection<ArticleTag> articleTags  = new ArrayList<>();
            for (Map<String, String> names : tags) {
                articleTags.add(articleTagRepository.findByName(names.get("name")));
            }
            articleContent.setArticleTags(articleTags);
            articleContent.setContent(content);
            articleContentRepository.save(articleContent);
            return 2001; // successfully
        }
    }

    public List<ArticleContent> findAllByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (lang == null) return null;
        else {
            return articleContentRepository.findAllByLanguage(language);
        }
    }

    public List<ArticleContent> findAllByUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;
        else {
            return articleContentRepository.findAllByUser(user);
        }
    }
}

/*
{
    "articleContents":  [
        {
            "content" : "jakis tekst po polsku",
            "title" : "jakis tytuł polski",
            "languages" : {
                "id" : 14
            }
        },
        {
            "content" : "some english stuff",
            "title" : "english is power",
            "languages" : {
                "id" : 13
            }
        }
    ],
    "articleTags" : [
        {
            "id" : 15
        }
    ]
}
 */