package com.cms.pp.cms.pp.Article;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

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



    public Object addArticleContent(ArticleContentDTO articleContentDTO) {
        ArticleContent articleContent = new ArticleContent();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");
            return errorProvidedDataHandler; //user not logged in
        }
        else {
            if (articleContentDTO.getTitle().equals("")) {
                errorProvidedDataHandler.setError("3001");
                return errorProvidedDataHandler; //title empty
            }
            if (articleContentDTO.getLanguage().equals("")) {
                errorProvidedDataHandler.setError("3002");
                return errorProvidedDataHandler; //lang empty
            }
            if (articleContentDTO.getTags().isEmpty()) {
                errorProvidedDataHandler.setError("3003");
                return errorProvidedDataHandler; //tags empty
            }
            if (articleContentDTO.getContent().equals("")) {
                errorProvidedDataHandler.setError("3004");
                return errorProvidedDataHandler; //content empty
            }

            /*

            output alert,
             */

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
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler; //success
        }

    }

    public ArticleContent getArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else {
            Long views = articleContent.getViews();
            views++;
            articleContent.setViews(views);
            articleContentRepository.save(articleContent);
            return articleContent;
        }

    }

    public Object changeArticleStatus(int id, String articleStatus) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleContent == null) {
            errorProvidedDataHandler.setError("3016"); //article not found.
            return errorProvidedDataHandler;
        }
        else {
            articleContent.setPublished(articleStatus);
            articleContentRepository.save(articleContent);
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler; //success
        }
    }

    public Object removeArticle(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleContent == null) {
            errorProvidedDataHandler.setError("3016"); //article not found.
            return errorProvidedDataHandler;
        }
        articleContentRepository.deleteById(id);
        errorProvidedDataHandler.setError("2001");//success
        return errorProvidedDataHandler;

    }

    public Object editArticle(Integer id, String title, String language, Collection<Map<String, String>> tags, String content) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleContent == null) {
            return "message.404";
        }
        if (title.equals("")) {
            errorProvidedDataHandler.setError("3001");
            return errorProvidedDataHandler; //title empty
        }
        if (language.equals("")) {
            errorProvidedDataHandler.setError("3002");
            return errorProvidedDataHandler; //language empty
        }
        if (tags.isEmpty()) {
            errorProvidedDataHandler.setError("3003");
            return errorProvidedDataHandler; //tags empty <= than 0 tags
        }
        if (content.equals("")) {
            errorProvidedDataHandler.setError("3004");
            return errorProvidedDataHandler; //content empty
        }
        articleContent.setTitle(title);
        articleContent.setLanguage(languageRepository.findByName(language));
        Collection<ArticleTag> articleTags  = new ArrayList<>();
        for (Map<String, String> names : tags) {
            articleTags.add(articleTagRepository.findByName(names.get("name")));
        }
        articleContent.setArticleTags(articleTags);
        articleContent.setContent(content);
        articleContentRepository.save(articleContent);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler; // successfully
    }

    public List<ArticleContent> findAll() {
        return articleContentRepository.findAll();
    }

    public List<ArticleContent> findAllByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (lang == null) {
            return null;
        }
        return articleContentRepository.findAllByLanguage(language);

    }

    public List<ArticleContent> findAllByUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;
        else {
            return articleContentRepository.findAllByUser(user);
        }
    }

    public Page<ArticleContent> findSomeArticlesByViews(int count) {
        return articleContentRepository.findAll(PageRequest.of(0,count, Sort.by("views").descending()));
    }

    public List<ArticleContent> findByTitleIgnoreCaseContaining(String title) {
        return articleContentRepository.findByTitleIgnoreCaseContaining(title);
    }

    public ArticleContent findByTitle(String title){
        return articleContentRepository.findByTitle(title);
    }

    public List<ArticleContent> findSomeArticlesByLazyLoading(int page, int size, String title) {
        Pageable pageableWithElements = PageRequest.of(page, size);
        return articleContentRepository.findByTitleIgnoreCaseContaining(title,  pageableWithElements);
    }

    public Object allowCommentsInArticle(int id, boolean allowComments) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            errorProvidedDataHandler.setError("3030");
            return errorProvidedDataHandler;
        }
        articleContent.setCommentsAllowed(allowComments);
        articleContentRepository.save(articleContent);
        errorProvidedDataHandler.setError("2001");
        return errorProvidedDataHandler;
    }
}
