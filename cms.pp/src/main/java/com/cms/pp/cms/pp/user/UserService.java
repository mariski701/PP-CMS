package com.cms.pp.cms.pp.user;


import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.ArticleContentRepository;
import com.cms.pp.cms.pp.Comment.Comment;
import com.cms.pp.cms.pp.Comment.CommentRepository;
import com.cms.pp.cms.pp.Comment.CommentsCountModel;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlags;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.Role.Role;
import com.cms.pp.cms.pp.Role.RoleRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
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
        User user = userRepository.findByUserMail(mail);
        if (user != null)
            return true;
        return false;
    }

    public boolean checkIfUserWithProvidedNameExists(String name) {
        User user = userRepository.findByUserName(name);
        if (user != null)
            return true;
        return false;
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public Object addUser(User user) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (configurationFlags.isRegister()) {

            if (user.getUserName().equals("")) {
                errorProvidedDataHandler.setError("3023");
                return errorProvidedDataHandler; //username not provided
            }
            if (user.getUserPassword().equals(""))
            {
                errorProvidedDataHandler.setError("3024"); //password empty
                return errorProvidedDataHandler;
            }
            if (user.getUserMail().equals("")) {
                errorProvidedDataHandler.setError("3025"); //usermail empty
                return errorProvidedDataHandler;
            }
            if (checkIfUserWithProvidedNameExists(user.getUserName())) {
                errorProvidedDataHandler.setError("3013"); //user already exists
                return errorProvidedDataHandler;
            }
            if (checkIfUserWithProvidedMailExists(user.getUserMail())) {
                errorProvidedDataHandler.setError("3011"); //usermail already used
                return errorProvidedDataHandler;
            }
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(Arrays.asList(userRole));
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            user.setEnabled(true);
            userRepository.save(user);
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError("4009");
            httpSession.invalidate();
            return errorProvidedDataHandler;
        }
    }

    public Object addCMSUser(CMSUserDTO cmsUserDTO) {
        if (!cmsUserDTO.getRole().equals("ROLE_ADMIN") && !cmsUserDTO.getRole().equals("ROLE_MODERATOR") && !cmsUserDTO.getRole().equals("ROLE_EDITOR")) {
            return "Wrong role";
        }
        if (cmsUserDTO.getUserName().equals("") || cmsUserDTO.getUserMail().equals("") || cmsUserDTO.getRole().isEmpty() || cmsUserDTO.getUserPassword().equals("")) {
            return "Lack of data";
        }
        else {
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

    }

    public Object deleteUser(int id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            errorProvidedDataHandler.setError("3028");
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
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler;
        }

    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public Object loginToService(String userMail, String password) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (configurationFlags.isLogin())
        {
            User user = userRepository.findByUserMail(userMail);
            if (user == null) {
                errorProvidedDataHandler.setError("3028");
                return errorProvidedDataHandler;
            }
            else {
                if (passwordEncoder.matches(password, user.getUserPassword())) {

                    myUserDetailsService.loadUserByUsername(userMail);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String currentPrincipalName =  authentication.getName();
                    System.out.println(currentPrincipalName + " logged to the service");
                    return user;
                }
                else {
                    errorProvidedDataHandler.setError("3029");
                    return errorProvidedDataHandler;
                }
            }
        }
        else {
            errorProvidedDataHandler.setError("4009");
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
        if (oldPassword.equals("")) {
            errorProvidedDataHandler.setError("3007"); //old password empty
            return errorProvidedDataHandler;
        }
        if (newPassword.equals("")) {
            errorProvidedDataHandler.setError("3008");//new password empty
            return errorProvidedDataHandler;
        }
        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");//user not logged in
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
                errorProvidedDataHandler.setError("2001"); //success
                return errorProvidedDataHandler;
            }
            else{
                errorProvidedDataHandler.setError("3009"); //wrong old password
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
        if (newUserMail.equals("")) {
            errorProvidedDataHandler.setError("3012");
            return errorProvidedDataHandler; //new mail empty
        }

        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");
            return errorProvidedDataHandler; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserMail(newUserMail);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserMail(newUserMail);
                errorProvidedDataHandler.setError("2001");
                userRepository.save(user);
                return errorProvidedDataHandler; //success
            }
            else {
                errorProvidedDataHandler.setError("3011");
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
        if (newUsername.equals("")) {
            errorProvidedDataHandler.setError("3006");
            return errorProvidedDataHandler; //new username empty
        }

        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");
            return errorProvidedDataHandler; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserName(newUsername);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserName(newUsername);
                errorProvidedDataHandler.setError("2001");
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
                errorProvidedDataHandler.setError("3013");
                return errorProvidedDataHandler; //username already used
            }
        }
    }

    public Object editUserRole(String roleName, int id) {
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        User user = userRepository.findById(id).orElse(null);
        Role role = roleRepository.findByName(roleName);
        if (user == null) {
            errorProvidedDataHandler.setError("3028");
            return errorProvidedDataHandler; //user not found
        }
        if (role == null) {
            errorProvidedDataHandler.setError("3020");
            return errorProvidedDataHandler; //role not found
        }

        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        user.setRoles(roleList);
        errorProvidedDataHandler.setError("2001");
        userRepository.save(user);
        return errorProvidedDataHandler; //success

    }

    public List<User> findCmsUsers() {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        Role roleEditor = roleRepository.findByName("ROLE_EDITOR");

        List<User> adminUsers = userRepository.findUserByRoles(roleAdmin);
        List<User> moderatorUsers = userRepository.findUserByRoles(roleModerator);
        List<User> editorUsers = userRepository.findUserByRoles(roleEditor);

        List<User> mergedList = new ArrayList<>();
        for (User user : adminUsers) {
            mergedList.add(user);
        }
        for (User user : moderatorUsers) {
            mergedList.add(user);
        }
        for (User user : editorUsers) {
            mergedList.add(user);
        }

        return mergedList;
    }



    public Object changeUserMail(int id, String newMail) {
        User user = userRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (newMail.equals("")) {
            errorProvidedDataHandler.setError("3012");
            return errorProvidedDataHandler;
        } // mail empty
        if (user == null) {
            errorProvidedDataHandler.setError("3028");
            return errorProvidedDataHandler;
        }
        else {
            if (!checkIfUserWithProvidedMailExists(newMail)) {
                user.setUserMail(newMail);
                errorProvidedDataHandler.setError("2001");
                userRepository.save(user);
                return errorProvidedDataHandler; //sukces
            }
            else {
                errorProvidedDataHandler.setError("3011");
                return errorProvidedDataHandler; //usermail already in database
            }

        }
    }

    public Object changeUserName(int id, String newUserName) {
        User user = userRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (newUserName.equals("")) {
            errorProvidedDataHandler.setError("3006");
            return errorProvidedDataHandler; //new nickname empty
        }
        if (user == null) {
            errorProvidedDataHandler.setError("3028");
            return errorProvidedDataHandler;
        }
        else {
            if (!checkIfUserWithProvidedNameExists(newUserName)) {
                user.setUserName(newUserName);
                userRepository.save(user);
                errorProvidedDataHandler.setError("2001");
                return errorProvidedDataHandler; //success
            }
            else
            {
                errorProvidedDataHandler.setError("3013");
                return errorProvidedDataHandler; //username already in database
            }

        }
    }

    public User findTheBestCommenter() {
        List<CommentsCountModel> commentsCountModels = commentRepository.countTotalCommentsByUser();
        User user = userRepository.findById(commentsCountModels.get(0).getCommentId()).orElse(null);
        if (user == null) {
            return null;
        }
        return user;
    }


}
