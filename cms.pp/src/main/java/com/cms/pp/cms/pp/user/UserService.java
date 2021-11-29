package com.cms.pp.cms.pp.user;


import com.cms.pp.cms.pp.Role.Role;
import com.cms.pp.cms.pp.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public User addUser(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(userRole));
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return user;
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

}
