package com.cms.pp.cms.pp;

import com.cms.pp.cms.pp.Alerts.AlertCode;
import com.cms.pp.cms.pp.Alerts.AlertCodeRepository;
import com.cms.pp.cms.pp.Alerts.AlertTranslation;
import com.cms.pp.cms.pp.Alerts.AlertTranslationRepository;
import com.cms.pp.cms.pp.Article.*;
import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.Comment.CommentRepository;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlags;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlagsRepository;
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
    private ArticleContentRepository articleContentRepository;
    @Autowired
    private ArticleTagRepository articleTagRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AlertCodeRepository alertCodeRepository;
    @Autowired
    private AlertTranslationRepository alertTranslationRepository;
    @Autowired
    private ConfigurationFlagsRepository configurationFlagsRepository;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        ConfigurationFlags configurationFlags = new ConfigurationFlags();
        configurationFlags.setComments(true);
        configurationFlags.setRegister(true);
        configurationFlags.setLogin(true);
        configurationFlagsRepository.save(configurationFlags);

        Privilege adminPanelAccessPrivilege = createPrivilegeIfNotFound("ADMIN_PANEL");
        Privilege changeConfigurationFlagsPrivilege = createPrivilegeIfNotFound("CONFIGURATION_FLAGS");
        Privilege removeTags = createPrivilegeIfNotFound("REMOVE_TAGS");
        Privilege addTagsCMS = createPrivilegeIfNotFound("ADD_TAGS_CMS");
        Privilege addLanguagePrivilege = createPrivilegeIfNotFound("ADD_LANGUAGE");
        Privilege removeLanguagePrivilege = createPrivilegeIfNotFound("REMOVE_LANGUAGE");
        Privilege editLanguagePrivilege = createPrivilegeIfNotFound("EDIT_LANGUAGE");
        Privilege editUserPrivilege  = createPrivilegeIfNotFound("EDIT_USER");
        Privilege editCMSUserPrivilege = createPrivilegeIfNotFound("EDIT_CMS_USER");
        Privilege removeUserPrivilege = createPrivilegeIfNotFound("REMOVE_USER");
        Privilege removeCMSUserPrivilege = createPrivilegeIfNotFound("REMOVE_CMS_USER");
        Privilege addTranslationPrivilege = createPrivilegeIfNotFound("ADD_TRANSLATION");
        Privilege removeTranslationPrivilege = createPrivilegeIfNotFound("REMOVE_TRANSLATION");
        Privilege editTranslationPrivilege = createPrivilegeIfNotFound("EDIT_TRANSLATION");
        Privilege readCMSUsersPrivilege = createPrivilegeIfNotFound("READ_CMS_USERS");
        Privilege createCMSUserPrivilege = createPrivilegeIfNotFound("CREATE_CMS_USER");
        Privilege changeRolePrivilege = createPrivilegeIfNotFound("CHANGE_ROLE");
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writeArticlePrivilege = createPrivilegeIfNotFound("WRITE_ARTICLE");
        Privilege writeCommentPrivilege = createPrivilegeIfNotFound("WRITE_COMMENT");
        Privilege removeCommentPrivilege = createPrivilegeIfNotFound("REMOVE_COMMENT");
        Privilege editCommentPrivilege = createPrivilegeIfNotFound("EDIT_COMMENT");
        Privilege editOwnCommentPrivilege = createPrivilegeIfNotFound("EDIT_OWN_COMMENT");
        Privilege editArticlesPrivilege = createPrivilegeIfNotFound("EDIT_ARTICLE");
        Privilege addRolePrivilege = createPrivilegeIfNotFound("ADD_ROLE");
        Privilege editRole = createPrivilegeIfNotFound("EDIT_ROLE");

        List<Privilege> adminPriviliges = Arrays.asList(editRole, addRolePrivilege, editTranslationPrivilege, removeTranslationPrivilege, addTranslationPrivilege, removeCMSUserPrivilege, removeUserPrivilege, editCMSUserPrivilege, editUserPrivilege, editLanguagePrivilege, removeLanguagePrivilege, addLanguagePrivilege, createCMSUserPrivilege, removeTags, changeConfigurationFlagsPrivilege, changeRolePrivilege, addTagsCMS, readCMSUsersPrivilege, readPrivilege, writeArticlePrivilege, writeCommentPrivilege, removeCommentPrivilege, editArticlesPrivilege, adminPanelAccessPrivilege, editCommentPrivilege, editOwnCommentPrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPriviliges);
        createRoleIfNotFound("ROLE_MODERATOR", Arrays.asList(editTranslationPrivilege, removeTranslationPrivilege, addTranslationPrivilege, removeUserPrivilege, editUserPrivilege, editLanguagePrivilege, removeLanguagePrivilege, addLanguagePrivilege, readCMSUsersPrivilege, changeConfigurationFlagsPrivilege, removeTags, readPrivilege, writeArticlePrivilege, writeCommentPrivilege, addTagsCMS, removeCommentPrivilege, editOwnCommentPrivilege, editCommentPrivilege, adminPanelAccessPrivilege));
        createRoleIfNotFound("ROLE_EDITOR", Arrays.asList(readPrivilege, removeTags, editOwnCommentPrivilege, writeArticlePrivilege, addTagsCMS, writeCommentPrivilege, editCommentPrivilege, editArticlesPrivilege, adminPanelAccessPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege, editOwnCommentPrivilege, writeCommentPrivilege, editCommentPrivilege));
        createRoleIfNotFound("ROLE_USERWITHOUTCOMMENTS", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role moderatorRole = roleRepository.findByName("ROLE_MODERATOR");
        Role editorRole = roleRepository.findByName("ROLE_EDITOR");
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setUserName("admin");
        user.setUserPassword(passwordEncoder.encode("admin"));
        user.setUserMail("admin@cms.pp.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);

        Language englishLanguage = createLanguageIfNotFound("english");
        Language polishLanguage = createLanguageIfNotFound("polish");
        englishLanguage.setLanguageCode("en_EN");
        polishLanguage.setLanguageCode("pl_PL");

        ArticleTag generalTag = createTagIfNotFound("general");
        generalTag.setLanguage(englishLanguage);
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        ArticleContent articleContentPolish = new ArticleContent();
        articleContentPolish.setContent("Test artykułu po polsku.");
        articleContentPolish.setTitle("tytuł");
        articleContentPolish.setPublished("PUBLISHED");
        articleContentPolish.setUser(user);
        articleContentPolish.setDate(date);
        articleContentPolish.setViews(255);
        ArticleContent articleContentEnglish = new ArticleContent();
        articleContentEnglish.setContent("Test of article in English.");
        articleContentEnglish.setTitle("title");
        articleContentEnglish.setPublished("UNPUBLISHED");
        articleContentEnglish.setUser(user);
        articleContentEnglish.setDate(date);
        articleContentEnglish.setViews(200);

        articleContentPolish.setLanguage(polishLanguage);
        articleContentEnglish.setLanguage(englishLanguage);


        articleContentPolish.setArticleTags(Arrays.asList(generalTag));
        generalTag.setArticlesContent(Arrays.asList(articleContentPolish));
        articleContentRepository.save(articleContentEnglish);
        articleContentRepository.save(articleContentPolish);

        Comment comment = new Comment();
        comment.setContent("testowy komentarz do artykułu");
        comment.setDate(date);
        comment.setArticleContent(articleContentPolish);
        comment.setUser(user);
        commentRepository.save(comment);


        AlertCode resourceNotFound = createAlertIfNotFound("Resource not found!", "message.404");
        AlertCode successAlert = createAlertIfNotFound("Success!", "message.2001");
        AlertCode titleEmpty = createAlertIfNotFound("Title field is empty", "message.3001");
        AlertCode languageEmpty = createAlertIfNotFound("Language is not chosen", "message.3002");
        AlertCode tagsEmpty = createAlertIfNotFound("Tags are not chosen", "message.3003");
        AlertCode contentEmpty = createAlertIfNotFound("Content is empty", "message.3004");
        AlertCode notLoggedIn = createAlertIfNotFound("You are not logged in", "message.3005");
        AlertCode userNotProvided = createAlertIfNotFound("Username not provided", "message.3006");
        AlertCode oldPasswordNotProvided = createAlertIfNotFound("Old password is not provided", "message.3007");
        AlertCode newPasswordNotProvided = createAlertIfNotFound("New password is not provided", "message.3008");
        AlertCode incorrectPassword = createAlertIfNotFound("Incorrect password", "message.3009");
        AlertCode mailAlreadyExists = createAlertIfNotFound("Provided mail already exists", "message.3011");
        AlertCode mailNotProvided = createAlertIfNotFound("Email address not provided", "message.3012");
        AlertCode usernameAlreadyExists = createAlertIfNotFound("Provided username already exists", "message.3013");
        AlertCode tagAlreadyExists = createAlertIfNotFound("Tag already exists!", "message.3014");
        AlertCode langaugeDoesntExists = createAlertIfNotFound("Provided language doesn't exists", "message.3015");

        alertCodeRepository.saveAll(Arrays.asList(usernameAlreadyExists, mailNotProvided, mailAlreadyExists, incorrectPassword, newPasswordNotProvided, oldPasswordNotProvided, userNotProvided, tagsEmpty, contentEmpty, titleEmpty, languageEmpty, successAlert, resourceNotFound, notLoggedIn, tagAlreadyExists, langaugeDoesntExists));

        AlertTranslation alertTranslation = new AlertTranslation();
        alertTranslation.setErrorTranslation("Zasób nie może zostać odnaleziony");
        alertTranslation.setLanguage(polishLanguage);
        alertTranslation.setAlertCode(resourceNotFound);
        alertTranslationRepository.save(alertTranslation);

        AlertTranslation alertTranslation2 = new AlertTranslation();
        alertTranslation2.setErrorTranslation("Sukces!");
        alertTranslation2.setLanguage(polishLanguage);
        alertTranslation2.setAlertCode(successAlert);
        alertTranslationRepository.save(alertTranslation2);

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

    @Transactional
    AlertCode createAlertIfNotFound(String name, String code) {
        AlertCode alertCode = alertCodeRepository.findByAlertName(name);
        if (alertCode == null) {
            alertCode = new AlertCode();
            alertCode.setAlertName(name);
            alertCode.setAlertCode(code);
            alertCodeRepository.save(alertCode);
        }
        return alertCode;
    }
}
