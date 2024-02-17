package com.cms.pp.cms.pp.service;


import com.cms.pp.cms.pp.model.CustomTopCommentersClass;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.model.CommentsCountModel;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.RoleName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Slf4j
public class UserService {
    public final static String ANONYMOUS_USER = "anonymousUser";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ConfigurationFlagsRepository configurationFlagsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleContentRepository articleContentRepository;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private MyUserDetailsService myUserDetailsService;



    public boolean checkIfUserWithProvidedMailExists(String mail) {
        return userRepository.findByUserMail(mail) != null;
    }

    public boolean checkIfUserWithProvidedNameExists(String name) {
        return userRepository.findByUserName(name) != null;
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public Object addUser(User user) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (configurationFlags.isRegister()) {

            if (user.getUserName().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3023.getValue());
                return errorProvidedDataHandler; //username not provided
            }
            if (user.getUserPassword().isEmpty())
            {
                errorProvidedDataHandler.setError(Code.CODE_3024.getValue()); //password empty
                return errorProvidedDataHandler;
            }
            if (user.getUserMail().isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3025.getValue()); //usermail empty
                return errorProvidedDataHandler;
            }
            if (checkIfUserWithProvidedNameExists(user.getUserName())) {
                errorProvidedDataHandler.setError(Code.CODE_3013.getValue()); //user already exists
                return errorProvidedDataHandler;
            }
            if (checkIfUserWithProvidedMailExists(user.getUserMail())) {
                errorProvidedDataHandler.setError(Code.CODE_3011.getValue()); //usermail already used
                return errorProvidedDataHandler;
            }
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER.getRoleName());
            user.setRoles(Collections.singletonList(userRole));
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            user.setEnabled(true);
            userRepository.save(user);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError(Code.CODE_4009.getValue());
            httpSession.invalidate();
            return errorProvidedDataHandler;
        }
    }

    public Object addCMSUser(CMSUserDTO cmsUserDTO) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (!(Arrays.asList(RoleName.ROLE_ADMIN.getRoleName(),
                        RoleName.ROLE_EDITOR.getRoleName(),
                        RoleName.ROLE_MODERATOR.getRoleName())
                .contains(cmsUserDTO.getRole()))) {
            errorProvidedDataHandler.setError(Code.CODE_3020.getValue());
            return errorProvidedDataHandler;
        }
        if (cmsUserDTO.getUserName().isEmpty())
        {
            errorProvidedDataHandler.setError(Code.CODE_3023.getValue());
            return errorProvidedDataHandler;
        }
        if (cmsUserDTO.getUserPassword().isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3024.getValue());
            return errorProvidedDataHandler;
        }

        if (checkIfUserWithProvidedNameExists(cmsUserDTO.getUserName())) {
            errorProvidedDataHandler.setError(Code.CODE_3013.getValue()); //user already exists
            return errorProvidedDataHandler;
        }
        if (checkIfUserWithProvidedMailExists(cmsUserDTO.getUserMail())) {
            errorProvidedDataHandler.setError(Code.CODE_3011.getValue()); //usermail already used
            return errorProvidedDataHandler;
        }

        User user = new User();
        user.setUserName(cmsUserDTO.getUserName());
        user.setUserMail(cmsUserDTO.getUserMail());
        Role userRole = roleRepository.findByName(cmsUserDTO.getRole());
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setUserPassword(passwordEncoder.encode(cmsUserDTO.getUserPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return user;


    }

    public Object deleteUser(int id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            errorProvidedDataHandler.setError(Code.CODE_3028.getValue());
            return errorProvidedDataHandler;
        }
        else {
            List<Comment> comments = commentRepository.findByUser(user);
            List<ArticleContent> articleContents = articleContentRepository.findAllByUser(user);

            for (ArticleContent articleContent : articleContents) {
                articleContent.setUser(null);
            }

            for (Comment comment : comments) {
                comment.setUser(null);
            }
            articleContentRepository.saveAll(articleContents);
            commentRepository.saveAll(comments);
            userRepository.deleteById(id);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }

    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public Object logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        java.util.Date date = new java.util.Date();
        log.info("[{}][USER]: {} logged out from service", date, username);
        ErrorProvidedDataHandler errorProvidedDataHandler  = new ErrorProvidedDataHandler();
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        httpSession.invalidate();
        return errorProvidedDataHandler;
    }

    public Object loginToService(String userMail, String password) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (configurationFlags.isLogin())
        {
            User user = userRepository.findByUserMail(userMail);
            if (user == null) {
                errorProvidedDataHandler.setError(Code.CODE_3028.getValue());
                return errorProvidedDataHandler;
            }
            else {
                if (passwordEncoder.matches(password, user.getUserPassword())) {
                    java.util.Date date = new java.util.Date();
                    myUserDetailsService.loadUserByUsername(userMail);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String currentPrincipalName =  authentication.getName();
                    log.info("[{}][USER]: {} logged into service", date, currentPrincipalName);
                    return user;
                }
                else {
                    errorProvidedDataHandler.setError(Code.CODE_3029.getValue());
                    return errorProvidedDataHandler;
                }
            }
        }
        else {
            errorProvidedDataHandler.setError(Code.CODE_4009.getValue());
            httpSession.invalidate();
            return errorProvidedDataHandler;
        }

    }

    public Object changePassword(String oldPassword, String newPassword) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (oldPassword.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3007.getValue()); //old password empty
            return errorProvidedDataHandler;
        }
        if (newPassword.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3008.getValue());//new password empty
            return errorProvidedDataHandler;
        }
        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());//user not logged in
            return errorProvidedDataHandler;
        }
        else
        {
            User user = userRepository.findByUserName(username);
            if (passwordEncoder.matches(oldPassword, user.getUserPassword())) {
                String newPasswordEncoded = passwordEncoder.encode(newPassword);
                user.setUserPassword(newPasswordEncoded);
                userRepository.save(user);
                Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword(), nowAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue()); //success
                return errorProvidedDataHandler;
            }
            else{
                errorProvidedDataHandler.setError(Code.CODE_3009.getValue()); //wrong old password
                return errorProvidedDataHandler;
            }
        }
    }

    public List<User> findByUserNameIgnoreCaseContaining(String userName) {
        return userRepository.findByUserNameIgnoreCaseContaining(userName, Sort.by("userName").ascending());
    }

    public List<User> findSomeUsersByLazyLoadingAndUserName(int page, int size, String username) {
        Pageable pageableWithElements = PageRequest.of(page, size, Sort.by("userName").ascending());
        return userRepository.findByUserNameIgnoreCaseContaining(username, pageableWithElements);

    }

    public Object editUserMail(String newUserMail) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (newUserMail.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3012.getValue());
            return errorProvidedDataHandler; //new mail empty
        }

        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserMail(newUserMail);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserMail(newUserMail);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                userRepository.save(user);
                return errorProvidedDataHandler; //success
            }
            else {
                errorProvidedDataHandler.setError(Code.CODE_3011.getValue());
                return errorProvidedDataHandler; //usermail already used
            }
        }
    }

    public Object editUserName(String newUsername) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (newUsername.isEmpty()) {
            //errorProvidedDataHandler.setError(Code.CODE_3006));
            errorProvidedDataHandler.setError(Code.CODE_3006.getValue());
            return errorProvidedDataHandler; //new username empty
        }

        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserName(newUsername);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserName(newUsername);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                userRepository.save(user);
                Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword(), nowAuthorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return errorProvidedDataHandler; //success
            }
            else {
                errorProvidedDataHandler.setError(Code.CODE_3013.getValue());
                return errorProvidedDataHandler; //username already used
            }
        }
    }

    public Object editUserRole(String roleName, int id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        User user = userRepository.findById(id).orElse(null);
        Role role = roleRepository.findByName(roleName);
        if (user == null) {
            errorProvidedDataHandler.setError(Code.CODE_3028.getValue());
            return errorProvidedDataHandler; //user not found
        }
        if (role == null) {
            errorProvidedDataHandler.setError(Code.CODE_3020.getValue());
            return errorProvidedDataHandler; //role not found
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoles(roleList);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        userRepository.save(user);
        return errorProvidedDataHandler; //success

    }

    public List<User> findCmsUsers() {
        Collection<String> roles = Arrays.asList(
                RoleName.ROLE_ADMIN.getRoleName(),
                RoleName.ROLE_MODERATOR.getRoleName(),
                RoleName.ROLE_EDITOR.getRoleName()
        );
        List<User> mergedList = new ArrayList<>();
        roles.forEach(role ->
            mergedList.addAll(userRepository.findUserByRoles(roleRepository.findByName(role)))
        );
        return mergedList;
    }



    public Object changeUserMail(int id, String newMail) {
        User user = userRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (newMail.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3012.getValue());
            return errorProvidedDataHandler;
        } // mail empty
        if (user == null) {
            errorProvidedDataHandler.setError(Code.CODE_3028.getValue());
            return errorProvidedDataHandler;
        }
        else {
            if (!checkIfUserWithProvidedMailExists(newMail)) {
                user.setUserMail(newMail);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                userRepository.save(user);
                return errorProvidedDataHandler; //sukces
            }
            else {
                errorProvidedDataHandler.setError(Code.CODE_3011.getValue());
                return errorProvidedDataHandler; //usermail already in database
            }

        }
    }

    public Object changeUserName(int id, String newUserName) {
        User user = userRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (newUserName.equals("")) {
            errorProvidedDataHandler.setError(Code.CODE_3006.getValue());
            return errorProvidedDataHandler; //new nickname empty
        }
        if (user == null) {
            errorProvidedDataHandler.setError(Code.CODE_3028.getValue());
            return errorProvidedDataHandler;
        }
        else {
            if (!checkIfUserWithProvidedNameExists(newUserName)) {
                user.setUserName(newUserName);
                userRepository.save(user);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                return errorProvidedDataHandler; //success
            }
            else
            {
                errorProvidedDataHandler.setError(Code.CODE_3013.getValue());
                return errorProvidedDataHandler; //username already in database
            }

        }
    }

    public List<CustomTopCommentersClass> findTheBestCommenter(int size) {
        Pageable pageableWithElements = PageRequest.of(0, size);
        List<CommentsCountModel> commentsCountModels = commentRepository.countTotalCommentsByUser(pageableWithElements);

        List<CustomTopCommentersClass> customTopCommentersClassList = new ArrayList<>();

        for (CommentsCountModel commentsCountModel : commentsCountModels) {
            customTopCommentersClassList.add(new CustomTopCommentersClass(
                    userRepository.findById(commentsCountModel.getCommentId()).orElse(null).getUserName(), commentsCountModel.getTotal()
            ));
        }

        if (customTopCommentersClassList.isEmpty()) {
            return null;
        }
        return customTopCommentersClassList;
    }


}
