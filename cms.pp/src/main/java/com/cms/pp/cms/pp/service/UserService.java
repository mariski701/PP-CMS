package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.RoleName;
import com.cms.pp.cms.pp.mapper.AddCMSUserMapper;
import com.cms.pp.cms.pp.model.CommentsCountModel;
import com.cms.pp.cms.pp.model.CustomTopCommentersClass;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.model.entity.*;
import com.cms.pp.cms.pp.repository.*;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.utils.PrincipalUtils;
import com.cms.pp.cms.pp.validator.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.*;

@Data
@RequiredArgsConstructor
@Service("UserService")
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfigurationFlagsRepository configurationFlagsRepository;
    private final CommentRepository commentRepository;
    private final ArticleContentRepository articleContentRepository;
    private final HttpSession httpSession;
    private final MyUserDetailsService myUserDetailsService;
    private final AddCMSUserMapper addCMSUserMapper;
    private final AddUserRequestValidator addUserRequestValidator;
    private final AddCMSUserRequestValidator addCMSUserRequestValidator;
    private final ChangePasswordRequestValidator changePasswordRequestValidator;
    private final EditUserMailRequestValidator editUserMailRequestValidator;
    private final EditUserNameRequestValidator editUserNameRequestValidator;
    private final ChangeUserMailRequestValidator changeUserMailRequestValidator;
    private final ChangeUserNameRequestValidator changeUserNameRequestValidator;


    private boolean checkIfUserWithProvidedMailExists(String mail) {
        return userRepository.findByUserMail(mail) != null;
    }

    private boolean checkIfUserWithProvidedNameExists(String name) {
        return userRepository.findByUserName(name) != null;
    }

    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Object addUser(User user) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        if (configurationFlags.isRegister()) {
            Object validateRequest = addUserRequestValidator.validateAddUser(user);
            if (validateRequest != null)
                return validateRequest;
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

    @Override
    public Object addCMSUser(CMSUserDTO cmsUserDTO) {
        Object validateRequest =  addCMSUserRequestValidator.validateAddCMSUser(cmsUserDTO);
        if (validateRequest != null)
            return validateRequest;
        if (checkIfUserWithProvidedNameExists(cmsUserDTO.getUserName()))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());
        if (checkIfUserWithProvidedMailExists(cmsUserDTO.getUserMail()))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
        User user = addCMSUserMapper.mapCMSUserDTOToUser(cmsUserDTO, Collections.singletonList(roleRepository.findByName(cmsUserDTO.getRole())));
        log.info("{} [{}][Add CMS User]: {} added new CMS User [username: {}], [userMail: {}], [userRole: {}]", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), user.getUserName(), user.getUserMail(), user.getRoles());
        return userRepository.save(user);
    }

    @Override
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
        log.info("{} [{}][delete User]: {} deleted user of id {}", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal()), id);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Object logout() {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        log.info("{} [{}][Logout]: {} logged out from service", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), username);
        httpSession.invalidate();
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public Object loginToService(String userMail, String password) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        if (configurationFlags.isLogin()) {
            User user = userRepository.findByUserMail(userMail);
            if (user == null)
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
            else {
                if (passwordEncoder.matches(password, user.getUserPassword())) {
                    myUserDetailsService.loadUserByUsername(userMail);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String currentPrincipalName =  authentication.getName();
                    log.info("{} [{}][Login]: {} logged into service", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), currentPrincipalName);
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

    @Override
    public Object changePassword(String oldPassword, String newPassword) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Object validateRequest = changePasswordRequestValidator.validateChangePassword(oldPassword, newPassword, username);
        if (validateRequest != null)
            return validateRequest;
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
            log.info("{} [{}][Change password]: User {} changed password", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), username);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3009.getValue());

    }

    @Override
    public List<User> findByUserNameIgnoreCaseContaining(String userName) {
        return userRepository.findByUserNameIgnoreCaseContaining(userName, Sort.by("userName").ascending());
    }

    @Override
    public List<User> findSomeUsersByLazyLoadingAndUserName(int page, int size, String username) {
        Pageable pageableWithElements = PageRequest.of(page, size, Sort.by("userName").ascending());
        return userRepository.findByUserNameIgnoreCaseContaining(username, pageableWithElements);

    }

    @Override
    public Object editUserMail(String newUserMail) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Object validateRequest = editUserMailRequestValidator.validateEditUserMail(newUserMail, username);
        if (validateRequest != null) return validateRequest;
        User isNewMailUsedByAnyUser = userRepository.findByUserMail(newUserMail);
        if (isNewMailUsedByAnyUser == null)
        {
            User user = userRepository.findByUserName(username)
                    .setUserMail(newUserMail);
            userRepository.save(user);
            log.info("{} [{}][Change mail]: User {} changed mail", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), username);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
    }

    @Override
    public Object editUserName(String newUsername) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Object validateRequest = editUserNameRequestValidator.validateEditUserName(newUsername, username);
        if (validateRequest != null) return validateRequest;
        User checkUser = userRepository.findByUserName(newUsername);
        if (checkUser == null) {
            User user = userRepository.findByUserName(username)
                    .setUserName(newUsername);
            userRepository.save(user);
            Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getAuthorities();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword(), nowAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("{} [{}][Change username]: User {} changed username", new java.util.Date(), RequestContextHolder.currentRequestAttributes().getSessionId(), username);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());

    }

    @Override
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

    @Override
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

    @Override
    public Object changeUserMail(int id, String newMail) {
        Object validateRequest = changeUserMailRequestValidator.validateChangeUserMail(newMail);
        if (validateRequest != null) return validateRequest;
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
        if (!checkIfUserWithProvidedMailExists(newMail)) {
            user.setUserMail(newMail);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3011.getValue());
    }

    @Override
    public Object changeUserName(int id, String newUserName) {
        Object validateRequest = changeUserNameRequestValidator.validateChangeUserName(newUserName);
        if (validateRequest != null) return validateRequest;
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3028.getValue());
        if (!checkIfUserWithProvidedNameExists(newUserName)) {
            user.setUserName(newUserName);
            userRepository.save(user);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3013.getValue());
    }

    @Override
    public List<CustomTopCommentersClass> findTheBestCommenter(int size) {
        Pageable pageableWithElements = PageRequest.of(0, size);
        List<CommentsCountModel> commentsCountModels = commentRepository.countTotalCommentsByUser(pageableWithElements);
        List<CustomTopCommentersClass> customTopCommentersClassList = new ArrayList<>();
        for (CommentsCountModel commentsCountModel : commentsCountModels) {
            customTopCommentersClassList.add(new CustomTopCommentersClass(
                    Objects.requireNonNull(userRepository.findById(commentsCountModel.getCommentId()).orElse(null)).getUserName(), commentsCountModel.getTotal()
            ));
        }
        if (customTopCommentersClassList.isEmpty())
            return null;
        return customTopCommentersClassList;
    }
}
