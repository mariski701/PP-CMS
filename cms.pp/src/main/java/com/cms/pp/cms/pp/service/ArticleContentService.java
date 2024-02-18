package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.ArticleStatus;
import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.PrivilegeName;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import com.cms.pp.cms.pp.model.dto.ArticleContentDTO;
import com.cms.pp.cms.pp.model.entity.ArticleTag;
import com.cms.pp.cms.pp.model.entity.Language;
import com.cms.pp.cms.pp.model.entity.Comment;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@RequiredArgsConstructor
@Service("ArticleContentService")
public class ArticleContentService implements IArticleContentService {
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final ArticleContentRepository articleContentRepository;
    private final LanguageRepository languageRepository;
    private final ArticleTagRepository articleTagRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;


    @Override
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
        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler;
        }
        else {
            if (articleContentDTO.getTitle().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3001.getValue());
                return errorProvidedDataHandler;
            }
            if (articleContentDTO.getLanguage().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3002.getValue());
                return errorProvidedDataHandler;
            }
            if (articleContentDTO.getTags().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3003.getValue());
                return errorProvidedDataHandler;
            }
            if (articleContentDTO.getContent().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3004.getValue());
                return errorProvidedDataHandler;
            }
            if (articleContentDTO.getImage().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3032.getValue());
                return errorProvidedDataHandler;
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
            articleContent.setPublished(ArticleStatus.UNPUBLSHED.getStatus());
            articleContent.setImage(articleContentDTO.getImage());
            articleContent.setCommentsAllowed(true);
            User user = userRepository.findByUserName(username);
            articleContent.setUser(user);
            articleContentRepository.save(articleContent);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }

    }

    @Override
    @Nullable public ArticleContent getArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return null;
        long views = articleContent.getViews();
        views++;
        articleContent.setViews(views);
        articleContentRepository.save(articleContent);
        return articleContent;
    }

    @Override
    public Object changeArticleStatus(int id, String articleStatus) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleStatus.equals(ArticleStatus.PUBLISHED.getStatus()) || articleStatus.equals(ArticleStatus.UNPUBLSHED.getStatus())) {
            if (articleContent == null) {
                errorProvidedDataHandler.setError(Code.CODE_3016.getValue());
                return errorProvidedDataHandler;
            }
            if (articleStatus.equals(ArticleStatus.PUBLISHED.getStatus()))
                articleContent.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            articleContent.setPublished(articleStatus);
            articleContentRepository.save(articleContent);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }
        else
        {
            errorProvidedDataHandler.setError(Code.CODE_3034.getValue());
            return errorProvidedDataHandler;
        }
    }

    @Override
    public Object removeArticle(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (articleContent == null) {
            errorProvidedDataHandler.setError(Code.CODE_3016.getValue());
            return errorProvidedDataHandler;
        }
        List<Comment> comments = commentRepository.findByArticleContent(articleContent);
        commentRepository.deleteAll(comments);
        articleContentRepository.deleteById(id);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;

    }

    @Override
    public Object editArticle(Integer id, String title, String language, Collection<Map<String, String>> tags, String content, String image) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        boolean canEditArticle = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler;
        }

        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);

        User editedArticleOfUser = userRepository.findById(articleContent.getUser().getId()).orElse(null);
        Role editedArticleUserRole = editedArticleOfUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> editedArticleUserPrivileges = roleRepository.findByName(editedArticleUserRole.getName()).getPrivileges();


        User principalUser = userRepository.findByUserName(username);
        Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
        
        if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName()))) {
            canEditArticle = editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName())) ||
                    editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName())) ||
                    editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()));
        }
        if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName())) &&
                principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName())) &&
                !(principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName())))) {
            canEditArticle = (editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName())) ||
                    editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()))) &&
                    !editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName()));
        }
        if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName())) &&
                !(principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName()))) &&
                !(principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName())))) {
            if (editedArticleUserPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName()))) {
                int editedArticleUserId = articleContent.getUser().getId();
                int principalId = principalUser.getId();
                canEditArticle = editedArticleUserId == principalId;
            }
        }
        if (canEditArticle) {
            if (articleContent == null) {
                errorProvidedDataHandler.setError(Code.CODE_3030.getValue());
                return errorProvidedDataHandler;
            }
            if (title.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3001.getValue());
                return errorProvidedDataHandler;
            }
            if (language.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3002.getValue());
                return errorProvidedDataHandler;
            }
            if (tags.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3003.getValue());
                return errorProvidedDataHandler;
            }
            if (content.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3004.getValue());
                return errorProvidedDataHandler;
            }
            if (image.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3032.getValue());
                return errorProvidedDataHandler;
            }
            articleContent.setTitle(title);
            articleContent.setLanguage(languageRepository.findByName(language));
            Collection<ArticleTag> articleTags  = new ArrayList<>();
            for (Map<String, String> names : tags) {
                articleTags.add(articleTagRepository.findByName(names.get("name")));
            }
            articleContent.setArticleTags(articleTags);
            articleContent.setContent(content);
            articleContent.setImage(image);
            articleContentRepository.save(articleContent);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }
        else
        {
            errorProvidedDataHandler.setError(Code.CODE_3033.getValue());
            return errorProvidedDataHandler;
        }
    }

    @Override
    public List<ArticleContent> findAll() {
        return articleContentRepository.findAll();
    }

    @Override
    public List<ArticleContent> findAllByLanguage(String lang) {
        Language language = languageRepository.findByName(lang);
        if (lang == null)
            return null;
        return articleContentRepository.findAllByLanguageAndPublished(language, ArticleStatus.PUBLISHED.getStatus() , Sort.by("id").descending());
    }

    @Override
    public List<ArticleContent> findAllByUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;
        return articleContentRepository.findAllByUser(user, Sort.by("id").descending());

    }

    @Override
    public List<ArticleContent> findSomeArticlesByViews(int count, String lang) {
        Language language = languageRepository.findByName(lang);
        if (language == null)
            return null;
        Pageable pageableWithElements = PageRequest.of(0, count, Sort.by("views").descending());
        return articleContentRepository.findAllByLanguageAndPublished(language, ArticleStatus.PUBLISHED.getStatus(),  pageableWithElements);
    }

    @Override
    public List<ArticleContent> findByTitleIgnoreCaseContaining(String title) {
        return articleContentRepository.findByTitleIgnoreCaseContaining(title, Sort.by("id").descending());
    }

    @Override
    public List<ArticleContent> findByTitleIgnoreCaseContainingOrByTags(String language, String title, List<Map<String, String>> tagNames) {
        Language lang = languageRepository.findByName(language);
        if (lang == null)
            return null;
        if (title.isEmpty() && tagNames !=null) {
            List<ArticleTag> articleTagList = new ArrayList<>();

            for (Map<String, String> tagName : tagNames) {
                articleTagList.add(articleTagRepository.findByName(tagName.get("name")));
            }

            List<List<ArticleContent>> articleContentList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                articleContentList.add(articleContentRepository.findByPublishedAndArticleTagsAndLanguage( ArticleStatus.PUBLISHED.getStatus(),articleTag, lang, Sort.by("id").descending()));
            }

            List<ArticleContent> temp = new ArrayList<>();

            for (int j = 0; j < articleContentList.size(); j++)
            {
                for (int i = 0; i < articleContentList.get(j).size(); i++)
                {
                    temp.add(articleContentList.get(j).get(i));
                }
            }

            for (int i = 0; i < temp.size(); i++) {
                System.out.println(temp.get(i).getId());
            }
            return temp;
        }

        if (tagNames == null) {
            return articleContentRepository.findByTitleIgnoreCaseContainingAndPublishedAndLanguage(title, ArticleStatus.PUBLISHED.getStatus(), lang, Sort.by("id").descending());
        }

        if (!title.isEmpty() && tagNames != null) {
            List<ArticleTag> articleTagList = new ArrayList<>();

            for (int i = 0; i < tagNames.size(); i++) {
                articleTagList.add(articleTagRepository.findByName(tagNames.get(i).get("name")));
            }

            List<List<ArticleContent>> articleContentList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                articleContentList.add(articleContentRepository.findByTitleIgnoreCaseContainingAndPublishedAndArticleTagsAndLanguage( title, ArticleStatus.PUBLISHED.getStatus() , articleTag, lang, Sort.by("title").ascending()));
            }

            List<ArticleContent> temp = new ArrayList<>();

            for (int j = 0; j < articleContentList.size(); j++)
            {
                for (int i = 0; i < articleContentList.get(j).size(); i++)
                {
                    temp.add(articleContentList.get(j).get(i));
                }
            }
            return temp;
        }
        return null;
    }

    @Override
    public ArticleContent findByTitle(String title){
        return articleContentRepository.findByTitle(title);
    }

    @Override
    public List<ArticleContent> findSomeArticlesByLazyLoading(int page, int size, String title) {
        Pageable pageableWithElements = PageRequest.of(page, size, Sort.by("id").descending());
        return articleContentRepository.findByTitleIgnoreCaseContaining(title,  pageableWithElements);
    }

    @Override
    public Object allowCommentsInArticle(int id, boolean allowComments) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            errorProvidedDataHandler.setError(Code.CODE_3030.getValue());
            return errorProvidedDataHandler;
        }
        articleContent.setCommentsAllowed(allowComments);
        articleContentRepository.save(articleContent);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        return errorProvidedDataHandler;
    }

    @Override
    public ArticleContent getArticleContentByCommentId(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment == null)
            return null;

        return articleContentRepository.findArticleContentByComments(comment);
    }

    @Override
    public List<ArticleContent> findByTag(String tagName) {
        ArticleTag articleTag = articleTagRepository.findByName(tagName);
        if (articleTag == null)
            return null;
        return articleContentRepository.findByPublishedAndArticleTags(ArticleStatus.PUBLISHED.getStatus() ,articleTag, Sort.by("id").descending());
    }

    @Override
    public List<ArticleContent> getAllForCMS() {
        return articleContentRepository.findAll(Sort.by("id").descending());
    }

    @Override
    public List<ArticleContent> getAllByUsersInCMS() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals(ANONYMOUS_USER)) {
            return null;
        }
        return articleContentRepository.findAllByUser(userRepository.findByUserName(username), Sort.by("id").descending());
    }
}
