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
import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = true;
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

        Language englishLanguage = createLanguageIfNotFound("english");
        Language polishLanguage = createLanguageIfNotFound("polish");
        englishLanguage.setLanguageCode("en_EN");
        polishLanguage.setLanguageCode("pl_PL");


        Privilege adminPanelAccessPrivilege = createPrivilegeIfNotFound("ADMIN_PANEL");
        Privilege removeTags = createPrivilegeIfNotFound("REMOVE_TAGS"); //
        Privilege addTagsCMS = createPrivilegeIfNotFound("ADD_TAGS_CMS"); //
        Privilege addLanguagePrivilege = createPrivilegeIfNotFound("ADD_LANGUAGE"); //
        Privilege removeLanguagePrivilege = createPrivilegeIfNotFound("REMOVE_LANGUAGE"); //
        Privilege editLanguagePrivilege = createPrivilegeIfNotFound("EDIT_LANGUAGE"); //
        Privilege editUserPrivilege  = createPrivilegeIfNotFound("EDIT_USER"); //
        Privilege editCMSUserPrivilege = createPrivilegeIfNotFound("EDIT_CMS_USER"); //
        Privilege removeUserPrivilege = createPrivilegeIfNotFound("REMOVE_USER"); //
        Privilege addTranslationPrivilege = createPrivilegeIfNotFound("ADD_TRANSLATION"); //
        Privilege removeTranslationPrivilege = createPrivilegeIfNotFound("REMOVE_TRANSLATION"); //
        Privilege editTranslationPrivilege = createPrivilegeIfNotFound("EDIT_TRANSLATION"); //
        Privilege readCMSUsersPrivilege = createPrivilegeIfNotFound("READ_CMS_USERS"); //
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE"); //
        Privilege writeCommentPrivilege = createPrivilegeIfNotFound("WRITE_COMMENT"); //
        Privilege removeCommentPrivilege = createPrivilegeIfNotFound("REMOVE_COMMENT");
        Privilege editCommentPrivilege = createPrivilegeIfNotFound("EDIT_COMMENT"); //
        Privilege editOwnCommentPrivilege = createPrivilegeIfNotFound("EDIT_OWN_COMMENT"); //
        Privilege editArticlesPrivilege = createPrivilegeIfNotFound("EDIT_ARTICLE"); //
        Privilege editAdminsArticlePrivilege = createPrivilegeIfNotFound("EDIT_ADMINS_ARTICLE"); //
        Privilege editModeratorsArticlePrivilege = createPrivilegeIfNotFound("EDIT_MODERATORS_ARTICLE"); //
        Privilege editEditorsArticlePrivilege = createPrivilegeIfNotFound("EDIT_EDITORS_ARTICLE"); //
        Privilege addRolePrivilege = createPrivilegeIfNotFound("ADD_ROLE"); //
        Privilege editRole = createPrivilegeIfNotFound("EDIT_ROLE"); //
        Privilege editUserRole = createPrivilegeIfNotFound("EDIT_USER_ROLE"); //
        Privilege addCmsUser = createPrivilegeIfNotFound("ADD_CMS_USER"); //
        Privilege addArticlePrivilege = createPrivilegeIfNotFound("ADD_ARTICLE"); //
        Privilege removeArticlePrivilege = createPrivilegeIfNotFound("REMOVE_ARTICLE"); //
        Privilege editTagPrivilege = createPrivilegeIfNotFound("EDIT_TAG"); //
        Privilege manageConfigFlagsPrivilege = createPrivilegeIfNotFound("MANAGE_CONFIG_FLAGS"); //
        Privilege removeRolePrivilege = createPrivilegeIfNotFound("REMOVE_ROLE"); //
        Privilege publishArticlePrivilege = createPrivilegeIfNotFound("PUBLISH_ARTICLE"); //

        List<Privilege> adminPrivileges = Arrays.asList(editUserRole, publishArticlePrivilege, removeRolePrivilege, manageConfigFlagsPrivilege, editTagPrivilege, addArticlePrivilege, removeArticlePrivilege, addCmsUser, editEditorsArticlePrivilege, editModeratorsArticlePrivilege, editAdminsArticlePrivilege, editRole, addRolePrivilege, editTranslationPrivilege, removeTranslationPrivilege, addTranslationPrivilege,  removeUserPrivilege, editCMSUserPrivilege, editUserPrivilege, editLanguagePrivilege, removeLanguagePrivilege, addLanguagePrivilege,  removeTags,  addTagsCMS, readCMSUsersPrivilege, readPrivilege,  writeCommentPrivilege, removeCommentPrivilege, editArticlesPrivilege, adminPanelAccessPrivilege, editCommentPrivilege, editOwnCommentPrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_MODERATOR", Arrays.asList(adminPanelAccessPrivilege, readPrivilege, removeUserPrivilege, editCMSUserPrivilege, readCMSUsersPrivilege, addArticlePrivilege, removeArticlePrivilege, publishArticlePrivilege, editModeratorsArticlePrivilege, editEditorsArticlePrivilege, editArticlesPrivilege, addTagsCMS, removeTags, editTagPrivilege, writeCommentPrivilege, editOwnCommentPrivilege, editCommentPrivilege, manageConfigFlagsPrivilege, editUserPrivilege));
        createRoleIfNotFound("ROLE_EDITOR", Arrays.asList(adminPanelAccessPrivilege, readPrivilege, addArticlePrivilege, editEditorsArticlePrivilege, editArticlesPrivilege, addTagsCMS, writeCommentPrivilege, editOwnCommentPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege, writeCommentPrivilege, editOwnCommentPrivilege, editUserPrivilege));
        createRoleIfNotFound("ROLE_USERWITHOUTCOMMENTS", Arrays.asList(readPrivilege));
        createRoleIfNotFound("ROLE_GUEST", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role moderatorRole = roleRepository.findByName("ROLE_MODERATOR");
        Role editorRole = roleRepository.findByName("ROLE_EDITOR");
        Role userRole = roleRepository.findByName("ROLE_USER");
        Role userWithoutCommentsRole = roleRepository.findByName("ROLE_USERWITHOUTCOMMENTS");

        User admin = new User();
        admin.setUserName("admin");
        admin.setUserPassword(passwordEncoder.encode("admin"));
        admin.setUserMail("admin@cms.pp.com");
        admin.setRoles(Arrays.asList(adminRole));
        admin.setEnabled(true);
        userRepository.save(admin);

        User user = new User();
        user.setUserName("user");
        user.setUserPassword(passwordEncoder.encode("user"));
        user.setUserMail("user@user.pl");
        user.setRoles(Arrays.asList(userRole));
        user.setEnabled(true);
        userRepository.save(user);

        User moderator = new User();
        moderator.setUserName("moderator");
        moderator.setUserMail("moderator@pp.cms.com");
        moderator.setUserPassword(passwordEncoder.encode("moderator"));
        moderator.setRoles(Arrays.asList(moderatorRole));
        moderator.setEnabled(true);
        userRepository.save(moderator);

        User editor = new User();
        editor.setUserName("editor");
        editor.setUserMail("editor@pp.cms.com");
        editor.setUserPassword(passwordEncoder.encode("editor"));
        editor.setRoles(Arrays.asList(editorRole));
        editor.setEnabled(true);
        userRepository.save(editor);

        User withoutcomments = new User();
        withoutcomments.setUserName("withoutcomments");
        withoutcomments.setUserMail("withoutcomments@pp.cms.com");
        withoutcomments.setUserPassword(passwordEncoder.encode("withoutcomments"));
        withoutcomments.setRoles(Arrays.asList(userWithoutCommentsRole));
        withoutcomments.setEnabled(true);
        userRepository.save(withoutcomments);



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
        articleContentPolish.setArticleTags(Arrays.asList(ogolnyTag));
        articleContentRepository.save(articleContentPolish);

        ArticleContent articleContentKulinaria = new ArticleContent();
        articleContentKulinaria.setContent("Wlej wodę do garnka, włącz kuchenkę, połóż na kuchence, jak woda będzie wrzeć to będzie ugotowana.");
        articleContentKulinaria.setTitle("Jak ugotować wodę?");
        articleContentKulinaria.setPublished("PUBLISHED");
        articleContentKulinaria.setUser(editor);
        articleContentKulinaria.setDate(date);
        articleContentKulinaria.setViews(2);
        articleContentKulinaria.setCommentsAllowed(true);
        articleContentKulinaria.setLanguage(polishLanguage);
        articleContentKulinaria.setArticleTags(Arrays.asList(kulinariaTag));
        articleContentRepository.save(articleContentKulinaria);

        ArticleContent articleContentEnglish = new ArticleContent();
        articleContentEnglish.setContent("Test of article in English.");
        articleContentEnglish.setTitle("title");
        articleContentEnglish.setPublished("UNPUBLISHED");
        articleContentEnglish.setUser(admin);
        articleContentEnglish.setDate(date);
        articleContentEnglish.setCommentsAllowed(true);
        articleContentEnglish.setViews(1);
        articleContentEnglish.setLanguage(englishLanguage);
        generalTag.setArticlesContent(Arrays.asList(articleContentEnglish));
        articleContentRepository.save(articleContentEnglish);


        ArticleContent articleContentCulinary = new ArticleContent();
        articleContentCulinary.setContent("To warm water pour the water into the pot, put the pot with the water on the stove, when the water is boiling the water will be boiled.");
        articleContentCulinary.setTitle("How to warm water");
        articleContentCulinary.setPublished("PUBLISHED");
        articleContentCulinary.setUser(editor);
        articleContentCulinary.setDate(date);
        articleContentCulinary.setCommentsAllowed(true);
        articleContentCulinary.setViews(1);
        articleContentCulinary.setLanguage(englishLanguage);
        articleContentCulinary.setArticleTags(Arrays.asList(culinaryTag));
        articleContentRepository.save(articleContentCulinary);

        Comment comment = new Comment();
        comment.setContent("Ale smaczna woda!");
        comment.setDate(date);
        comment.setArticleContent(articleContentKulinaria);
        comment.setUser(user);
        commentRepository.save(comment);

        Comment comment2 = new Comment();
        comment2.setContent("Niesamowite jak smaczna potrafi być ta woda, dzięki!");
        comment2.setDate(date);
        comment2.setArticleContent(articleContentKulinaria);
        comment2.setUser(moderator);
        commentRepository.save(comment2);

        Comment comment3 = new Comment();
        comment3.setContent("Amazing taste of the water bro!");
        comment3.setDate(date);
        comment3.setArticleContent(articleContentCulinary);
        comment3.setUser(moderator);
        commentRepository.save(comment3);

        Comment comment4 = new Comment();
        comment4.setContent("Ugh that's ugly man!");
        comment4.setDate(date);
        comment4.setArticleContent(articleContentCulinary);
        comment4.setUser(user);
        commentRepository.save(comment4);

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
        AlertCode alertCode = alertCodeRepository.findByAlertCode(code);
        if (alertCode == null) {
            alertCode = new AlertCode();
            alertCode.setAlertName(name);
            alertCode.setAlertCode(code);
            alertCodeRepository.save(alertCode);
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
     //   }
        return alertTranslation;
    }
}

