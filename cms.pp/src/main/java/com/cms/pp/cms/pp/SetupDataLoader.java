package com.cms.pp.cms.pp;

import com.cms.pp.cms.pp.Article.*;
import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.Comment.CommentRepository;
import com.cms.pp.cms.pp.Priviliges.Privilege;
import com.cms.pp.cms.pp.Priviliges.PrivilegeRepository;
import com.cms.pp.cms.pp.Role.Role;
import com.cms.pp.cms.pp.Role.RoleRepository;
import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleContentRepository articleContentRepository;
    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private CommentRepository commentRepository;


    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege writeCommentPrivilege = createPrivilegeIfNotFound("WRITE_COMMENT");
        Privilege removeCommentPrivilege = createPrivilegeIfNotFound("REMOVE_COMMENT");
        Privilege editCommentPrivilege = createPrivilegeIfNotFound("EDIT_COMMENT");
        Privilege editArticlesPrivilege = createPrivilegeIfNotFound("EDIT_ARTICLE");
        Privilege adminPanelAccess = createPrivilegeIfNotFound("ADMIN_PANEL");

        List<Privilege> adminPriviliges = Arrays.asList(readPrivilege, writePrivilege, writeCommentPrivilege, removeCommentPrivilege, editArticlesPrivilege, adminPanelAccess, editCommentPrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPriviliges);
        createRoleIfNotFound("ROLE_MODERATOR", Arrays.asList(readPrivilege, writePrivilege, writeCommentPrivilege, removeCommentPrivilege, editCommentPrivilege));
        createRoleIfNotFound("ROLE_EDITOR", Arrays.asList(readPrivilege, writePrivilege, writeCommentPrivilege, editCommentPrivilege, editArticlesPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege, writeCommentPrivilege, editCommentPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role moderatorRole = roleRepository.findByName("ROLE_MODERATOR");
        Role editorRole = roleRepository.findByName("ROLE_EDITOR");
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setUserName("admin");
        user.setUserPassword(passwordEncoder.encode("admin"));
        user.setUserMail("admin@cms.pp.com");
        user.setRoles(Arrays.asList(adminRole, moderatorRole, editorRole, userRole));
        user.setEnabled(true);
        userRepository.save(user);

        Language englishLanguage = createLanguageIfNotFound("English");
        Language polishLanguage = createLanguageIfNotFound("Polish");

        ArticleTag generalTag = createTagIfNotFound("general");

        ArticleContent articleContentPolish = new ArticleContent();
        articleContentPolish.setContent("Test artykułu po polsku.");
        articleContentPolish.setTitle("Tytuł");
        ArticleContent articleContentEnglish = new ArticleContent();
        articleContentEnglish.setContent("Test of article in English.");
        articleContentEnglish.setTitle("title");

        articleContentPolish.setLanguages(polishLanguage);
        articleContentEnglish.setLanguages(englishLanguage);
        articleContentRepository.save(articleContentEnglish);
        articleContentRepository.save(articleContentPolish);

        Article article = new Article();
        articleContentPolish.setArticle(article);
        articleContentEnglish.setArticle(article);
        generalTag.setArticles(Arrays.asList(article));
        article.setArticleTags(Arrays.asList(generalTag));
        article.setArticleContents(Arrays.asList(articleContentPolish, articleContentEnglish));
        article.setUser(user);
        article.setPublished(true);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        article.setDate(date);
        //article.setViews(0);
        articleRepository.save(article);

        Comment comment = new Comment();
        comment.setContent("testowy komentarz do artykułu");
        comment.setDate(date);
        comment.setArticle(article);
        comment.setUser(user);
        commentRepository.save(comment);

        alreadySetup = true;

    }

    @Transactional
    public ArticleTag createTagIfNotFound(String name) {
        ArticleTag articleTag = articleTagRepository.findByName(name);
        if (articleTag == null) {
            articleTag = new ArticleTag();
            articleTag.setName(name);
            articleTagRepository.save(articleTag);
        }
        return articleTag;
    }

    @Transactional
    public Language createLanguageIfNotFound(String name) {
        Language language = languageRepository.findByName(name);
        if (language == null) {
            language = new Language();
            language.setName(name);
            languageRepository.save(language);
        }
        return language;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
