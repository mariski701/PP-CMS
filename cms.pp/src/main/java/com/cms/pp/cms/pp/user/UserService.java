package com.cms.pp.cms.pp.user;


import com.cms.pp.cms.pp.Role.Role;
import com.cms.pp.cms.pp.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
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

    public User addUser(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(userRole));
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }

    public User addCMSUser(CMSUserDTO cmsUserDTO) {
        if (!cmsUserDTO.getRole().equals("ROLE_ADMIN") && !cmsUserDTO.getRole().equals("ROLE_MODERATOR") && !cmsUserDTO.getRole().equals("ROLE_EDITOR")) {
            return null;
        }
        if (cmsUserDTO.getUserName().equals("") || cmsUserDTO.getUserMail().equals("") || cmsUserDTO.getRole().isEmpty() || cmsUserDTO.getUserPassword().equals("")) {
            return null;
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

    public String deleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "";
        }
        else {
            userRepository.deleteById(id);
            return "Deleted.";
        }

    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public User loginToService(String userMail, String password) {
        User user = userRepository.findByUserMail(userMail);
        if (user == null) {
            return null;
        }
        else {
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentPrincipalName =  authentication.getName();
                return user;
            }
            else
                return null;

        }
    }

    public int changePassword(String oldPassword, String newPassword) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (oldPassword.equals("")) {
            return 3007; //old password empty
        }
        if (newPassword.equals("")) {
            return 3008; //new password empty
        }
        if (username.equals("anonymousUser")) {
            return 3005; //user not logged in
        }
        else
        {
            User user = userRepository.findByUserName(username);
            if (passwordEncoder.matches(oldPassword, user.getUserPassword())) {
                String newPasswordEncoded = passwordEncoder.encode(newPassword);
                user.setUserPassword(newPasswordEncoded);
                return 2001;
            }
            else{
                return 3009; //wrong old password
            }
        }
    }

    public List<User> findByUserNameIgnoreCaseContaining(String userName) {
        return userRepository.findByUserNameIgnoreCaseContaining(userName);
    }

    public int editUserMail(String newUserMail) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (newUserMail.equals("")) {
            return 3012; //new mail empty
        }

        if (username.equals("anonymousUser")) {
            return 3005; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserMail(newUserMail);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserMail(newUserMail);
                userRepository.save(user);
                return 2001; //success
            }
            else {
                return 3011; //usermail already used
            }
        }
    }

    public int editUserName(String newUsername) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (newUsername.equals("")) {
            return 3006; //new username empty
        }

        if (username.equals("anonymousUser")) {
            return 3005; //user not logged in
        }
        else {
            User checkUser = userRepository.findByUserName(newUsername);
            if (checkUser == null)
            {
                User user = userRepository.findByUserName(username);
                user.setUserName(newUsername);
                userRepository.save(user);
                return 2001; //success
            }
            else {
                return 3010; //username already used
            }
        }
    }

    public int editUserRole(String roleName, int id) {
        User user = userRepository.findById(id).orElse(null);
        Role role = roleRepository.findByName(roleName);
        if (role == null || user == null) {
            return HttpStatus.NOT_FOUND.value(); //not found 404
        }
        else {
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            user.setRoles(roleList);
            userRepository.save(user);
            return 2001; //success
        }
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

    public boolean checkIfUserWithProvidedMailExists(String mail) {
        User user = userRepository.findByUserMail(mail);
        if (user == null)
            return false;
        return true;
    }

    public boolean checkIfUserWithProvidedNameExists(String name) {
        User user = userRepository.findByUserName(name);
        if (user == null)
            return false;
        return true;
    }

    public int changeUserMail(int id, String newMail) {
        User user = userRepository.findById(id).orElse(null);
        if (newMail.equals("")) {return 3012;} // mail empty
        if (user == null) {
            return HttpStatus.NOT_FOUND.value();
        }
        else {
            if (!checkIfUserWithProvidedMailExists(newMail)) {
                user.setUserMail(newMail);
                userRepository.save(user);
                return 2001; //sukces
            }
            else
                return 3011; //usermail already in database

        }
    }

    public int changeUserName(int id, String newUserName) {
        User user = userRepository.findById(id).orElse(null);
        if (newUserName.equals("")) {
            return 3006; //new nickname empty
        }
        if (user == null) {
            return HttpStatus.NOT_FOUND.value();
        }
        else {
            if (!checkIfUserWithProvidedNameExists(newUserName)) {
                user.setUserName(newUserName);
                userRepository.save(user);
                return 2001; //success
            }
            else
                return 3013; //username already in database

        }
    }


}
