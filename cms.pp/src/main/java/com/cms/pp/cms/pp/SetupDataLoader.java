package com.cms.pp.cms.pp;

import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.repository.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.enums.PrivilegeName;
import com.cms.pp.cms.pp.enums.RoleName;
import com.cms.pp.cms.pp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
/*
* Class used to generate data.
* Should be started at first application start.
* To generate data change alreadySetup flag to false.
* After first application start remember to change alreadySetup flag to false.
* !important: Remember to change generated users passwords
*/
@Component
@Slf4j
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
        log.info("Setup data loader started generating data...");

        ConfigurationFlags configurationFlags = new ConfigurationFlags();
        configurationFlags.setComments(true);
        configurationFlags.setRegister(true);
        configurationFlags.setLogin(true);
        configurationFlagsRepository.save(configurationFlags);

        log.info("Configuration flags had been generated: \n{}", configurationFlags);

        Language englishLanguage = createLanguageIfNotFound("english");
        Language polishLanguage = createLanguageIfNotFound("polish");
        englishLanguage.setLanguageCode("en_EN");
        polishLanguage.setLanguageCode("pl_PL");

        // note: keep in sync with PrivilegeName
        Privilege adminPanelAccessPrivilege = createPrivilegeIfNotFound(PrivilegeName.ADMIN_PANEL.getPrivilegeName());
        Privilege removeTags = createPrivilegeIfNotFound(PrivilegeName.REMOVE_TAGS.getPrivilegeName());
        Privilege addTagsCMS = createPrivilegeIfNotFound(PrivilegeName.ADD_TAGS_CMS.getPrivilegeName());
        Privilege addLanguagePrivilege = createPrivilegeIfNotFound(PrivilegeName.ADD_LANGUAGE.getPrivilegeName());
        Privilege removeLanguagePrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_LANGUAGE.getPrivilegeName());
        Privilege editLanguagePrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_LANGUAGE.getPrivilegeName());
        Privilege editUserPrivilege  = createPrivilegeIfNotFound(PrivilegeName.EDIT_USER.getPrivilegeName());
        Privilege editCMSUserPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_CMS_USER.getPrivilegeName());
        Privilege removeUserPrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_USER.getPrivilegeName());
        Privilege addTranslationPrivilege = createPrivilegeIfNotFound(PrivilegeName.ADD_TRANSLATION.getPrivilegeName());
        Privilege removeTranslationPrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_TRANSLATION.getPrivilegeName());
        Privilege editTranslationPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_TRANSLATION.getPrivilegeName());
        Privilege readCMSUsersPrivilege = createPrivilegeIfNotFound(PrivilegeName.READ_CMS_USERS.getPrivilegeName());
        Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeName.READ_PRIVILEGE.getPrivilegeName());
        Privilege writeCommentPrivilege = createPrivilegeIfNotFound(PrivilegeName.WRITE_COMMENT.getPrivilegeName());
        Privilege removeCommentPrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_COMMENT.getPrivilegeName());
        Privilege editCommentPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_COMMENT.getPrivilegeName());
        Privilege editOwnCommentPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_OWN_COMMENT.getPrivilegeName());
        Privilege editArticlesPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_ARTICLE.getPrivilegeName());
        Privilege editAdminsArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_ADMINS_ARTICLE.getPrivilegeName());
        Privilege editModeratorsArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_MODERATORS_ARTICLE.getPrivilegeName());
        Privilege editEditorsArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_EDITORS_ARTICLE.getPrivilegeName());
        Privilege addRolePrivilege = createPrivilegeIfNotFound(PrivilegeName.ADD_ROLE.getPrivilegeName());
        Privilege editRole = createPrivilegeIfNotFound(PrivilegeName.EDIT_ROLE.getPrivilegeName());
        Privilege editUserRole = createPrivilegeIfNotFound(PrivilegeName.EDIT_USER_ROLE.getPrivilegeName());
        Privilege addCmsUser = createPrivilegeIfNotFound(PrivilegeName.ADD_CMS_USER.getPrivilegeName());
        Privilege addArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.ADD_ARTICLE.getPrivilegeName());
        Privilege removeArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_ARTICLE.getPrivilegeName());
        Privilege editTagPrivilege = createPrivilegeIfNotFound(PrivilegeName.EDIT_TAG.getPrivilegeName());
        Privilege manageConfigFlagsPrivilege = createPrivilegeIfNotFound(PrivilegeName.MANAGE_CONFIG_FLAGS.getPrivilegeName());
        Privilege removeRolePrivilege = createPrivilegeIfNotFound(PrivilegeName.REMOVE_ROLE.getPrivilegeName());
        Privilege publishArticlePrivilege = createPrivilegeIfNotFound(PrivilegeName.PUBLISH_ARTICLE.getPrivilegeName());
        Privilege administrationUsersPrivilegeAccess = createPrivilegeIfNotFound(PrivilegeName.ADMIN_USERS_PRIVILEGE_ACCESS.getPrivilegeName());

        List<Privilege> adminPrivileges = Arrays.asList(administrationUsersPrivilegeAccess, editUserRole, publishArticlePrivilege, removeRolePrivilege, manageConfigFlagsPrivilege, editTagPrivilege, addArticlePrivilege, removeArticlePrivilege, addCmsUser, editEditorsArticlePrivilege, editModeratorsArticlePrivilege, editAdminsArticlePrivilege, editRole, addRolePrivilege, editTranslationPrivilege, removeTranslationPrivilege, addTranslationPrivilege,  removeUserPrivilege, editCMSUserPrivilege, editUserPrivilege, editLanguagePrivilege, removeLanguagePrivilege, addLanguagePrivilege,  removeTags,  addTagsCMS, readCMSUsersPrivilege, readPrivilege,  writeCommentPrivilege, removeCommentPrivilege, editArticlesPrivilege, adminPanelAccessPrivilege, editCommentPrivilege, editOwnCommentPrivilege);
        createRoleIfNotFound(RoleName.ROLE_ADMIN.getRoleName(), adminPrivileges);
        createRoleIfNotFound(RoleName.ROLE_MODERATOR.getRoleName(), Arrays.asList(adminPanelAccessPrivilege, readPrivilege, removeUserPrivilege, editCMSUserPrivilege, readCMSUsersPrivilege, addArticlePrivilege, removeArticlePrivilege, publishArticlePrivilege, editModeratorsArticlePrivilege, editEditorsArticlePrivilege, editArticlesPrivilege, writeCommentPrivilege, editOwnCommentPrivilege, editCommentPrivilege, manageConfigFlagsPrivilege, editUserPrivilege));
        createRoleIfNotFound(RoleName.ROLE_EDITOR.getRoleName(), Arrays.asList(adminPanelAccessPrivilege, readPrivilege, addArticlePrivilege, editEditorsArticlePrivilege, editArticlesPrivilege, writeCommentPrivilege, editOwnCommentPrivilege));
        createRoleIfNotFound(RoleName.ROLE_USER.getRoleName(), Arrays.asList(readPrivilege, writeCommentPrivilege, editOwnCommentPrivilege, editUserPrivilege));
        createRoleIfNotFound(RoleName.ROLE_USERWITHOUTCOMMENTS.getRoleName(), Collections.singletonList(readPrivilege));
        createRoleIfNotFound(RoleName.ROLE_GUEST.getRoleName(), Collections.singletonList(readPrivilege));

        User admin = new User();
        admin.setUserName("admin");
        admin.setUserPassword(passwordEncoder.encode("admin"));
        admin.setUserMail("admin@cms.pp.com");
        admin.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_ADMIN.getRoleName())));
        admin.setEnabled(true);
        userRepository.save(admin);
        log.info("User admin had been generated: \n{}", admin);

        User user = new User();
        user.setUserName("user");
        user.setUserPassword(passwordEncoder.encode("user"));
        user.setUserMail("user@user.pl");
        user.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_USER.getRoleName())));
        user.setEnabled(true);
        userRepository.save(user);
        log.info("User user had been generated: \n{}", user);

        User moderator = new User();
        moderator.setUserName("moderator");
        moderator.setUserMail("moderator@pp.cms.com");
        moderator.setUserPassword(passwordEncoder.encode("moderator"));
        moderator.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_MODERATOR.getRoleName())));
        moderator.setEnabled(true);
        userRepository.save(moderator);
        log.info("User moderator had been generated: \n{}", moderator);

        User editor = new User();
        editor.setUserName("editor");
        editor.setUserMail("editor@pp.cms.com");
        editor.setUserPassword(passwordEncoder.encode("editor"));
        editor.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_EDITOR.getRoleName())));
        editor.setEnabled(true);
        userRepository.save(editor);
        log.info("User editor had been generated: \n{}", editor);

        User withoutcomments = new User();
        withoutcomments.setUserName("withoutcomments");
        withoutcomments.setUserMail("withoutcomments@pp.cms.com");
        withoutcomments.setUserPassword(passwordEncoder.encode("withoutcomments"));
        withoutcomments.setRoles(Collections.singletonList(roleRepository.findByName(RoleName.ROLE_USERWITHOUTCOMMENTS.getRoleName())));
        withoutcomments.setEnabled(true);
        userRepository.save(withoutcomments);
        log.info("User withoutcomments had been generated: \n{}", withoutcomments);

        ArticleTag generalTag = createTagIfNotFound("general");
        generalTag.setLanguage(englishLanguage);

        ArticleTag culinaryTag = createTagIfNotFound("culinary");
        culinaryTag.setLanguage(englishLanguage);

        ArticleTag ogolnyTag = createTagIfNotFound("ogólny");
        ogolnyTag.setLanguage(polishLanguage);

        ArticleTag kulinariaTag = createTagIfNotFound("kulinaria");
        kulinariaTag.setLanguage(polishLanguage);

        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        ArticleContent articleContentPolish = new ArticleContent();
        articleContentPolish.setContent("Test artykułu po polsku.");
        articleContentPolish.setTitle("tytuł");
        articleContentPolish.setPublished("UNPUBLISHED");
        articleContentPolish.setUser(admin);
        articleContentPolish.setDate(date);
        articleContentPolish.setViews(2);
        articleContentPolish.setCommentsAllowed(true);
        articleContentPolish.setLanguage(polishLanguage);
        articleContentPolish.setArticleTags(Collections.singletonList(ogolnyTag));
        articleContentRepository.save(articleContentPolish);
        log.info("articleContentPolish had been generated: \n{}", articleContentPolish);

        ArticleContent articleContentKulinaria = new ArticleContent();
        articleContentKulinaria.setContent("Wlej wodę do garnka, włącz kuchenkę, połóż na kuchence, jak woda będzie wrzeć to będzie ugotowana.");
        articleContentKulinaria.setTitle("Jak ugotować wodę?");
        articleContentKulinaria.setPublished("PUBLISHED");
        articleContentKulinaria.setUser(editor);
        articleContentKulinaria.setDate(date);
        articleContentKulinaria.setViews(2);
        articleContentKulinaria.setCommentsAllowed(true);
        articleContentKulinaria.setLanguage(polishLanguage);
        articleContentKulinaria.setArticleTags(Collections.singletonList(kulinariaTag));
        articleContentRepository.save(articleContentKulinaria);
        log.info("articleContentKulinaria had been generated: \n{}", articleContentKulinaria);

        ArticleContent articleContentEnglish = new ArticleContent();
        articleContentEnglish.setContent("Test of article in English.");
        articleContentEnglish.setTitle("title");
        articleContentEnglish.setPublished("UNPUBLISHED");
        articleContentEnglish.setUser(admin);
        articleContentEnglish.setDate(date);
        articleContentEnglish.setCommentsAllowed(true);
        articleContentEnglish.setViews(1);
        articleContentEnglish.setLanguage(englishLanguage);
        generalTag.setArticlesContent(Collections.singletonList(articleContentEnglish));
        articleContentRepository.save(articleContentEnglish);
        log.info("articleContentEnglish had been generated: \n{}", articleContentEnglish);


        ArticleContent articleContentCulinary = new ArticleContent();
        articleContentCulinary.setContent("To warm water pour the water into the pot, put the pot with the water on the stove, when the water is boiling the water will be boiled.");
        articleContentCulinary.setTitle("How to warm water");
        articleContentCulinary.setPublished("PUBLISHED");
        articleContentCulinary.setUser(editor);
        articleContentCulinary.setDate(date);
        articleContentCulinary.setCommentsAllowed(true);
        articleContentCulinary.setViews(1);
        articleContentCulinary.setLanguage(englishLanguage);
        articleContentCulinary.setArticleTags(Collections.singletonList(culinaryTag));
        articleContentRepository.save(articleContentCulinary);
        log.info("articleContentCulinary had been generated: \n{}", articleContentCulinary);


        Comment comment = new Comment();
        comment.setContent("Ale smaczna woda!");
        comment.setDate(date);
        comment.setArticleContent(articleContentKulinaria);
        comment.setUser(user);
        commentRepository.save(comment);
        log.info("comment had been generated: \n{}", comment);


        Comment comment2 = new Comment();
        comment2.setContent("Niesamowite jak smaczna potrafi być ta woda, dzięki!");
        comment2.setDate(date);
        comment2.setArticleContent(articleContentKulinaria);
        comment2.setUser(moderator);
        commentRepository.save(comment2);
        log.info("comment2 had been generated: \n{}", comment2);

        Comment comment3 = new Comment();
        comment3.setContent("Amazing taste of the water bro!");
        comment3.setDate(date);
        comment3.setArticleContent(articleContentCulinary);
        comment3.setUser(moderator);
        commentRepository.save(comment3);
        log.info("comment3 had been generated: \n{}", comment3);

        Comment comment4 = new Comment();
        comment4.setContent("Ugh that's ugly man!");
        comment4.setDate(date);
        comment4.setArticleContent(articleContentCulinary);
        comment4.setUser(user);
        commentRepository.save(comment4);
        log.info("comment4 had been generated: \n{}", comment4);

        AlertCode buttonAddComment = createAlertIfNotFound("Add Comment", "button.add-comment");
        AlertCode buttonCancel = createAlertIfNotFound("Cancel", "button.cancel");
        AlertCode buttonChange = createAlertIfNotFound("Change", "button.change");
        AlertCode buttonDelete = createAlertIfNotFound("Delete", "button.delete");
        AlertCode buttonEdit = createAlertIfNotFound("Edit", "button.edit");
        AlertCode buttonEditComment = createAlertIfNotFound("Edit comment", "button.edit-comment");
        AlertCode buttonLogin = createAlertIfNotFound("Login", "button.login");
        AlertCode buttonNo = createAlertIfNotFound("No", "button.no");
        AlertCode buttonProceed = createAlertIfNotFound("Proceed", "button.proceed");
        AlertCode buttonReadArticle = createAlertIfNotFound("Read on", "button.read-article");
        AlertCode buttonRegister = createAlertIfNotFound("Register", "button.register");
        AlertCode buttonSearch = createAlertIfNotFound("Search", "button.search");
        AlertCode buttonSelect = createAlertIfNotFound("Select", "button.select");
        AlertCode buttonSettings = createAlertIfNotFound("Settings", "button.settings");
        AlertCode buttonYes = createAlertIfNotFound("Yes", "button.yes");
        AlertCode headerArticles = createAlertIfNotFound("Articles", "header.articles");
        AlertCode headerChangeEmail = createAlertIfNotFound("Change mail", "header.change-email");
        AlertCode headerChangePassword = createAlertIfNotFound("Change password", "header.change-password");
        AlertCode headerChangeUsername = createAlertIfNotFound("Change username", "header.change-username");
        AlertCode headerLeaveComment = createAlertIfNotFound("Leave comment", "header.leave-comment");
        AlertCode headerPopular = createAlertIfNotFound("Popular", "header.popular");
        AlertCode headerTags = createAlertIfNotFound("Tags", "header.tags");
        AlertCode headerUserComments = createAlertIfNotFound("User comments", "header.user-comments");
        AlertCode headerUsers = createAlertIfNotFound("Users", "header.users");
        AlertCode homePageActiveUsers = createAlertIfNotFound("Active users", "home-page.active-users");
        AlertCode homePageInspiringArticles = createAlertIfNotFound("Inspiring articles", "home-page.inspiring-articles");
        AlertCode homePageInterestingComments = createAlertIfNotFound("Interesting comments", "home-page.interesting-comments");
        AlertCode homePageMostPopularArticles = createAlertIfNotFound("Most popular articles in this month", "home-page.most-popular-articles");
        AlertCode homePageTopUsers = createAlertIfNotFound("The best commenters", "home-page.top-users");
        AlertCode labelArticleName = createAlertIfNotFound("Article title", "label.article-name");
        AlertCode labelComments = createAlertIfNotFound("Comments", "label.comments");
        AlertCode labelCommentsCount = createAlertIfNotFound("Comments count", "label.comments-count");
        AlertCode labelEmail = createAlertIfNotFound("Email", "label.email");
        AlertCode labelEmailConfirm = createAlertIfNotFound("Confirm email", "label.email-confirm");
        AlertCode labelNewEmail = createAlertIfNotFound("New email", "label.new-email");
        AlertCode labelNewPassword = createAlertIfNotFound("New password", "label.new-password");
        AlertCode labelNewPasswordConfirm = createAlertIfNotFound("Confirm new password", "label.new-password-confirm");
        AlertCode labelOldPassword = createAlertIfNotFound("Old password", "label.old-password");
        AlertCode labelPassword = createAlertIfNotFound("Password", "label.password");
        AlertCode labelPasswordConfirm = createAlertIfNotFound("Confirm password", "label.password-confirm");
        AlertCode labelUsername = createAlertIfNotFound("Username", "label.username");
        AlertCode messageEmailInUse = createAlertIfNotFound("Provided e-mail is already used by other user", "message.email-in-use");
        AlertCode messageEmailChangeSuccess = createAlertIfNotFound("Successful change of email address", "message.email-change-success");
        AlertCode messagePasswordChangeSuccess = createAlertIfNotFound("Successful password change", "message.password-change-success");
        AlertCode messageUnidentifiedError = createAlertIfNotFound("An unexpected error has occurred", "message.unidentified-error");
        AlertCode messageUsernameChangeSuccess = createAlertIfNotFound("Successful username change", "message.username-change-success");
        AlertCode messageUsernameInUse = createAlertIfNotFound("Provided username is already used by other user", "message.username-in-use");
        AlertCode messageWrongLoginData = createAlertIfNotFound("Provided e-mail address or password is incorrect", "message.wrong-login-data");
        AlertCode messageWrongOldPassword = createAlertIfNotFound("Wrong old password", "message.wrong-old-password");
        AlertCode modalDeleteCommentDialogTitle = createAlertIfNotFound("Do you want to delete this comment?", "modal.delete-comment-dialog.title");
        AlertCode modalLoginTitle = createAlertIfNotFound("Sign in", "modal.login.title");
        AlertCode modalRegisterTitle = createAlertIfNotFound("Register", "modal.register.title");
        AlertCode modalSelectTagsTitle = createAlertIfNotFound("Select tags", "modal.select-tags.title");
        AlertCode modalNavbarArticles = createAlertIfNotFound("Articles", "navbar.articles");
        AlertCode navbarCms = createAlertIfNotFound("Administration panel", "navbar.cms");
        AlertCode navbarHomePage = createAlertIfNotFound("Home", "navbar.home-page");
        AlertCode navbarLanguage = createAlertIfNotFound("Language", "navbar.language");
        AlertCode navbarLogin = createAlertIfNotFound("Sign in", "navbar.login");
        AlertCode navbarLogout = createAlertIfNotFound("Log out", "navbar.logout");
        AlertCode navbarRegister = createAlertIfNotFound("Register", "navbar.register");
        AlertCode navbarUsers = createAlertIfNotFound("Users", "navbar.users");
        AlertCode validationEmail = createAlertIfNotFound("Incorrect e-mail address", "validation.email");
        AlertCode validationEmailWrongConfirm = createAlertIfNotFound("Incorrectly repeated e-mail address", "validation.email-wrong-confirm");
        AlertCode validationPasswordWrongConfirm = createAlertIfNotFound("Old password is incorrect", "validation.password-wrong-confirm");
        AlertCode validationRequired = createAlertIfNotFound("Field is required", "validation.required");



        alertCodeRepository.saveAll(Arrays.asList(buttonAddComment, buttonCancel, buttonChange, buttonDelete, buttonEdit, buttonEditComment, buttonLogin, buttonNo, buttonProceed,
                buttonReadArticle, buttonRegister, buttonSearch, buttonSelect, buttonSettings, buttonYes, headerArticles, headerChangeEmail, headerChangePassword, headerChangeUsername, headerLeaveComment,
                headerPopular, headerTags, headerUserComments, headerUsers, homePageActiveUsers, homePageInspiringArticles, homePageInterestingComments, homePageMostPopularArticles,
                homePageTopUsers, labelArticleName, labelComments, labelCommentsCount, labelEmail, labelEmailConfirm, labelNewEmail, labelNewPassword, labelNewPasswordConfirm,
                labelOldPassword, labelPassword, labelPasswordConfirm, labelUsername, messageEmailInUse, messageEmailChangeSuccess, messagePasswordChangeSuccess, messageUnidentifiedError,
                messageUsernameChangeSuccess, messageUsernameInUse, messageWrongLoginData, messageWrongOldPassword, modalDeleteCommentDialogTitle, modalLoginTitle, modalRegisterTitle,
                modalSelectTagsTitle, modalNavbarArticles, navbarCms, navbarHomePage, navbarLanguage, navbarLogin, navbarLogout, navbarRegister, navbarUsers, validationEmail, validationEmailWrongConfirm,
                validationPasswordWrongConfirm, validationRequired));



        AlertTranslation buttonAddCommentTranslation = createAlertTranslationIfNotFound(buttonAddComment, polishLanguage, "Dodaj komentarz");
        AlertTranslation buttonCancelTranslation = createAlertTranslationIfNotFound(buttonCancel, polishLanguage, "Anuluj");
        AlertTranslation buttonChangeTranslation = createAlertTranslationIfNotFound(buttonChange, polishLanguage, "Zmień");
        AlertTranslation buttonDeleteTranslation = createAlertTranslationIfNotFound(buttonDelete, polishLanguage, "Usuń");
        AlertTranslation buttonEditTranslation = createAlertTranslationIfNotFound(buttonEdit, polishLanguage, "Edytuj");
        AlertTranslation buttonEditCommentTranslation = createAlertTranslationIfNotFound(buttonEditComment, polishLanguage, "Edytuj komentarz");
        AlertTranslation buttonLoginTranslation = createAlertTranslationIfNotFound(buttonLogin, polishLanguage, "Zaloguj się");
        AlertTranslation buttonNoTranslation = createAlertTranslationIfNotFound(buttonNo, polishLanguage, "Nie");
        AlertTranslation buttonProceedTranslation = createAlertTranslationIfNotFound(buttonProceed, polishLanguage, "Przejdź");
        AlertTranslation buttonReadArticleTranslation = createAlertTranslationIfNotFound(buttonReadArticle, polishLanguage, "Czytaj dalej");
        AlertTranslation buttonRegisterTranslation = createAlertTranslationIfNotFound(buttonRegister, polishLanguage, "Zarejestruj się");
        AlertTranslation buttonSearchTranslation = createAlertTranslationIfNotFound(buttonSearch, polishLanguage, "Szukaj");
        AlertTranslation buttonSelectTranslation = createAlertTranslationIfNotFound(buttonSelect, polishLanguage, "Wybierz");
        AlertTranslation buttonSettingsTranslation = createAlertTranslationIfNotFound(buttonSettings, polishLanguage, "Ustawienia");
        AlertTranslation buttonYesTranslation = createAlertTranslationIfNotFound(buttonYes, polishLanguage, "Tak");
        AlertTranslation headerArticlesTranslation = createAlertTranslationIfNotFound(headerArticles, polishLanguage, "Artykuły");
        AlertTranslation headerChangeEmailTranslation = createAlertTranslationIfNotFound(headerChangeEmail, polishLanguage, "Zmiana adresu email");
        AlertTranslation headerChangePasswordTranslation = createAlertTranslationIfNotFound(headerChangePassword, polishLanguage, "Zmiana hasła");
        AlertTranslation headerChangeUsernameTranslation = createAlertTranslationIfNotFound(headerChangeUsername, polishLanguage, "Zmiana nazwy użytkownika");
        AlertTranslation headerLeaveCommentTranslation = createAlertTranslationIfNotFound(headerLeaveComment, polishLanguage, "Zostaw komentarz");
        AlertTranslation headerPopularTranslation = createAlertTranslationIfNotFound(headerPopular, polishLanguage, "Popularne");
        AlertTranslation headerTagsTranslation = createAlertTranslationIfNotFound(headerTags, polishLanguage, "Tagi");
        AlertTranslation headerUserCommentsTranslation = createAlertTranslationIfNotFound(headerUserComments, polishLanguage, "Komentarze użytkownika");
        AlertTranslation headerUsersTranslation = createAlertTranslationIfNotFound(headerUsers, polishLanguage, "Użytkownicy serwisu");
        AlertTranslation homePageActiveUsersTranslation = createAlertTranslationIfNotFound(homePageActiveUsers, polishLanguage, "aktywnych użytkowników");
        AlertTranslation homePageInspiringArticlesTranslation = createAlertTranslationIfNotFound(homePageInspiringArticles, polishLanguage, "inspirujących artykułów");
        AlertTranslation homePageInterestingCommentsTranslation = createAlertTranslationIfNotFound(homePageInterestingComments, polishLanguage, "ciekawych komentarzy");
        AlertTranslation homePageMostPopularArticlesTranslation = createAlertTranslationIfNotFound(homePageMostPopularArticles, polishLanguage, "Najpopularniejsze w tym miesiącu");
        AlertTranslation homePageTopUsersTranslation = createAlertTranslationIfNotFound(homePageTopUsers, polishLanguage, "Najlepsi komentujący");
        AlertTranslation labelArticleNameTranslation = createAlertTranslationIfNotFound(labelArticleName, polishLanguage, "Tytuł artykułu");
        AlertTranslation labelCommentsTranslation = createAlertTranslationIfNotFound(labelComments, polishLanguage, "Komentarze");
        AlertTranslation labelCommentsCountTranslation = createAlertTranslationIfNotFound(labelCommentsCount, polishLanguage, "Liczba komentarzy");
        AlertTranslation labelEmailTranslation = createAlertTranslationIfNotFound(labelEmail, polishLanguage, "Email");
        AlertTranslation labelEmailConfirmTranslation = createAlertTranslationIfNotFound(labelEmailConfirm, polishLanguage, "Powtórz email");
        AlertTranslation labelNewEmailTranslation = createAlertTranslationIfNotFound(labelNewEmail, polishLanguage, "Nowy adres email");
        AlertTranslation labelNewPasswordTranslation = createAlertTranslationIfNotFound(labelNewPassword, polishLanguage, "Nowe hasło");
        AlertTranslation labelNewPasswordConfirmTranslation = createAlertTranslationIfNotFound(labelNewPasswordConfirm, polishLanguage, "Powtórz nowe hasło");
        AlertTranslation labelOldPasswordTranslation = createAlertTranslationIfNotFound(labelOldPassword, polishLanguage, "Stare hasło");
        AlertTranslation labelPasswordTranslation = createAlertTranslationIfNotFound(labelPassword, polishLanguage, "Hasło");
        AlertTranslation labelPasswordConfirmTranslation = createAlertTranslationIfNotFound(labelPasswordConfirm, polishLanguage, "Powtórz hasło");
        AlertTranslation labelUsernameTranslation = createAlertTranslationIfNotFound(labelUsername, polishLanguage, "Nazwa użytkownika");
        AlertTranslation messageEmailInUseTranslation = createAlertTranslationIfNotFound(messageEmailInUse, polishLanguage, "Istnieje już konto zarejestrowane na ten mail");
        AlertTranslation messageEmailChangeSuccessTranslation = createAlertTranslationIfNotFound(messageEmailChangeSuccess, polishLanguage, "Udało się zmienić email");
        AlertTranslation messagePasswordChangeSuccessTranslation = createAlertTranslationIfNotFound(messagePasswordChangeSuccess, polishLanguage, "Udało się zmienić hasło");
        AlertTranslation messageUnidentifiedErrorTranslation = createAlertTranslationIfNotFound(messageUnidentifiedError, polishLanguage, "Wystąpił nieoczekiwany błąd");
        AlertTranslation messageUsernameChangeSuccessTranslation = createAlertTranslationIfNotFound(messageUsernameChangeSuccess, polishLanguage, "Udało się zmienić nazwę użytkownika");
        AlertTranslation messageUsernameInUseTranslation = createAlertTranslationIfNotFound(messageUsernameInUse, polishLanguage, "Nazwa użytkownika zajęta");
        AlertTranslation messageWrongLoginDataTranslation = createAlertTranslationIfNotFound(messageWrongLoginData, polishLanguage, "Email lub hasło jest nieprawidłowe");
        AlertTranslation messageWrongOldPasswordTranslation = createAlertTranslationIfNotFound(messageWrongOldPassword, polishLanguage, "Nieprawidłowe stare hasło");
        AlertTranslation modalDeleteCommentDialogTitleTranslation = createAlertTranslationIfNotFound(modalDeleteCommentDialogTitle, polishLanguage, "Czy chcesz usunąć ten komentarz?");
        AlertTranslation modalLoginTitleTranslation = createAlertTranslationIfNotFound(modalLoginTitle, polishLanguage, "Logowanie");
        AlertTranslation modalRegisterTitleTranslation = createAlertTranslationIfNotFound(modalRegisterTitle, polishLanguage, "Rejestracja");
        AlertTranslation modalSelectTagsTitleTranslation = createAlertTranslationIfNotFound(modalSelectTagsTitle, polishLanguage, "Wybierz tagi");
        AlertTranslation modalNavbarArticlesTranslation = createAlertTranslationIfNotFound(modalNavbarArticles, polishLanguage, "Artykuły");
        AlertTranslation navbarCmsTranslation = createAlertTranslationIfNotFound(navbarCms, polishLanguage, "Panel administracyjny");
        AlertTranslation navbarHomePageTranslation = createAlertTranslationIfNotFound(navbarHomePage, polishLanguage, "Strona główna");
        AlertTranslation navbarLanguageTranslation = createAlertTranslationIfNotFound(navbarLanguage, polishLanguage, "Język");
        AlertTranslation navbarLoginTranslation = createAlertTranslationIfNotFound(navbarLogin, polishLanguage, "Zaloguj");
        AlertTranslation navbarLogoutTranslation = createAlertTranslationIfNotFound(navbarLogout, polishLanguage, "Wyloguj");
        AlertTranslation navbarRegisterTranslation = createAlertTranslationIfNotFound(navbarRegister, polishLanguage, "Zarejestruj");
        AlertTranslation navbarUsersTranslation = createAlertTranslationIfNotFound(navbarUsers, polishLanguage, "Użytkownicy");
        AlertTranslation validationEmailTranslation = createAlertTranslationIfNotFound(validationEmail, polishLanguage, "Nieprawidłowy email");
        AlertTranslation validationEmailWrongConfirmTranslation = createAlertTranslationIfNotFound(validationEmailWrongConfirm, polishLanguage, "Źle powtórzono email");
        AlertTranslation validationPasswordWrongConfirmTranslation = createAlertTranslationIfNotFound(validationPasswordWrongConfirm, polishLanguage, "Źle powtórzono hasło");
        AlertTranslation validationRequiredTranslation = createAlertTranslationIfNotFound(validationRequired, polishLanguage, "Pole jest wymagane");

        alertTranslationRepository.saveAll(Arrays.asList(buttonAddCommentTranslation, buttonCancelTranslation, buttonChangeTranslation, buttonDeleteTranslation, buttonEditTranslation, buttonEditCommentTranslation,
                buttonLoginTranslation, buttonNoTranslation, buttonProceedTranslation, buttonReadArticleTranslation, buttonRegisterTranslation, buttonSearchTranslation, buttonSelectTranslation, buttonSettingsTranslation,
                buttonYesTranslation, headerArticlesTranslation, headerChangeEmailTranslation, headerChangePasswordTranslation, headerChangeUsernameTranslation, headerLeaveCommentTranslation, headerPopularTranslation, headerTagsTranslation,
                headerUserCommentsTranslation, headerUsersTranslation, homePageActiveUsersTranslation, homePageInspiringArticlesTranslation, homePageInterestingCommentsTranslation, homePageMostPopularArticlesTranslation,
                homePageTopUsersTranslation, labelArticleNameTranslation, labelCommentsTranslation, labelCommentsCountTranslation, labelEmailTranslation, labelEmailConfirmTranslation, labelNewEmailTranslation,
                labelNewPasswordTranslation, labelNewPasswordConfirmTranslation, labelOldPasswordTranslation, labelPasswordTranslation, labelPasswordConfirmTranslation, labelUsernameTranslation, messageEmailInUseTranslation,
                messageEmailChangeSuccessTranslation, messagePasswordChangeSuccessTranslation, messageUnidentifiedErrorTranslation, messageUsernameChangeSuccessTranslation, messageUsernameInUseTranslation,
                messageWrongLoginDataTranslation, messageWrongOldPasswordTranslation, modalDeleteCommentDialogTitleTranslation, modalLoginTitleTranslation, modalRegisterTitleTranslation, modalSelectTagsTitleTranslation,
                modalNavbarArticlesTranslation, navbarCmsTranslation, navbarHomePageTranslation, navbarLanguageTranslation, navbarLoginTranslation, navbarLogoutTranslation, navbarRegisterTranslation, navbarUsersTranslation,
                validationEmailTranslation, validationEmailWrongConfirmTranslation, validationPasswordWrongConfirmTranslation, validationRequiredTranslation
        ));

        alreadySetup = true;

    }

    @Transactional
    public ArticleTag createTagIfNotFound(String name) {
        ArticleTag articleTag = articleTagRepository.findByName(name);
        if (articleTag == null) {
            articleTag = new ArticleTag();
            articleTag.setName(name);
            articleTagRepository.save(articleTag);
            log.info("Article tag had been generated: \n{}", articleTag);
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
            log.info("Language had been generated: \n{}", language);
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
            log.info("Privilege had been generated: \n{}", privilege);
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
            log.info("Role had been generated: \n{}", role);
        }
        return role;
    }

    @Transactional
    AlertCode createAlertIfNotFound(String name, String code) {
        AlertCode alertCode = alertCodeRepository.findByAlertCode(code);
        if (alertCode == null) {
            alertCode = new AlertCode();
            alertCode.setAlertName(name);
            alertCode.setAlertCode(code);
            alertCodeRepository.save(alertCode);
            log.info("AlertCode had been generated: \n{}", alertCode);
        }
        return alertCode;
    }

    @Transactional
    AlertTranslation createAlertTranslationIfNotFound(AlertCode alertCode, Language language, String translation) {
       // AlertTranslation alertTranslation = alertTranslationRepository.findByErrorTranslation(translation);
      //  if (alertTranslation == null) {
           AlertTranslation alertTranslation = new AlertTranslation();
            alertTranslation.setAlertCode(alertCode);
            alertTranslation.setLanguage(language);
            alertTranslation.setErrorTranslation(translation);
            alertTranslationRepository.save(alertTranslation);
        log.info("Alert translation had been generated: \n{}", alertTranslation);
     //   }
        return alertTranslation;
    }
}

