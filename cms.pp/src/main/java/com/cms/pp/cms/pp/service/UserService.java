package com.cms.pp.cms.pp.service;


import com.cms.pp.cms.pp.model.CustomTopCommentersClass;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.model.CommentsCountModel;
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.RoleName;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.utils.PrincipalUtils;
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
    private ErrorProvidedDataHandlerUtils errorProvidedDataHandlerUtils;
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
        if (configurationFlags.isRegister()) {

            if (user.getUserName().isEmpty())
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3023.getValue());
            if (user.getUserPassword().isEmpty())
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3024.getValue());
            if (user.getUserMail().isEmpty())
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3025.getValue());
            if (checkIfUserWithProvidedNameExists(user.getUserName()))
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());
            if (checkIfUserWithProvidedMailExists(user.getUserMail()))
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
            Role userRole = roleRepository.findByName(RoleName.ROLE_USER.getRoleName());
            user.setRoles(Collections.singletonList(userRole));
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            user.setEnabled(true);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        else {
            httpSession.invalidate();
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_4009.getValue());
        }
    }

    public Object addCMSUser(CMSUserDTO cmsUserDTO) {
        if (!(Arrays.asList(RoleName.ROLE_ADMIN.getRoleName(),
                        RoleName.ROLE_EDITOR.getRoleName(),
                        RoleName.ROLE_MODERATOR.getRoleName())
                .contains(cmsUserDTO.getRole()))) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3020.getValue());
        }
        if (cmsUserDTO.getUserName().isEmpty())
        {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3023.getValue());
        }
        if (cmsUserDTO.getUserPassword().isEmpty()) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3024.getValue());
        }

        if (checkIfUserWithProvidedNameExists(cmsUserDTO.getUserName())) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());
        }
        if (checkIfUserWithProvidedMailExists(cmsUserDTO.getUserMail())) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
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
        User user = userRepository.findById(id).orElse(null);

        if (user == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());

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
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public Object logout() {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        java.util.Date date = new java.util.Date();
        log.info("[{}][USER]: {} logged out from service", date, username);
        ErrorProvidedDataHandler errorProvidedDataHandler  = new ErrorProvidedDataHandler();
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        httpSession.invalidate();
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3024.getValue());
    }

    public Object loginToService(String userMail, String password) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        if (configurationFlags.isLogin()) {
            User user = userRepository.findByUserMail(userMail);
            if (user == null) {
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
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
                    return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3029.getValue());
                }
            }
        }
        httpSession.invalidate();
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_4009.getValue());
    }

    public Object changePassword(String oldPassword, String newPassword) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (oldPassword.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3007.getValue());

        if (newPassword.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3008.getValue());

        if (username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());

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
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        else{
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3009.getValue());
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
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (newUserMail.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3012.getValue());

        if (username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
        User checkUser = userRepository.findByUserMail(newUserMail);
        if (checkUser == null)
        {
            User user = userRepository.findByUserName(username);
            user.setUserMail(newUserMail);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
    }

    public Object editUserName(String newUsername) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (newUsername.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3006.getValue());
        if (username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());

        User checkUser = userRepository.findByUserName(newUsername);
        if (checkUser == null)
        {
            User user = userRepository.findByUserName(username);
            user.setUserName(newUsername);
            userRepository.save(user);
            Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getAuthorities();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword(), nowAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());

    }

    public Object editUserRole(String roleName, int id) {
        User user = userRepository.findById(id).orElse(null);
        Role role = roleRepository.findByName(roleName);
        if (user == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
        if (role == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3020.getValue());
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoles(roleList);
        userRepository.save(user);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());

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
        if (newMail.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3012.getValue());
        if (user == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
        if (!checkIfUserWithProvidedMailExists(newMail)) {
            user.setUserMail(newMail);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
    }

    public Object changeUserName(int id, String newUserName) {
        User user = userRepository.findById(id).orElse(null);
        if (newUserName.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3006.getValue());
        if (user == null) 
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
        if (!checkIfUserWithProvidedNameExists(newUserName)) {
            user.setUserName(newUserName);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());
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
        if (customTopCommentersClassList.isEmpty())
            return null;
        return customTopCommentersClassList;
    }


}
