package com.cms.pp.cms.pp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/user/")
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    HttpSession httpSession ;
    @Autowired
    MyUserDetailsService myUserDetailsService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @RolesAllowed({"ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_EDITOR"})
    @GetMapping("getusers")
    public List<User> getUsers() {
        return userService.getUsers();
    }


    @PostMapping("login")
    public User login(@RequestBody Map<String, String> body ) {
       // return myUserDetailsService.loadUserByUsername(body.get("userMail"));
        return userService.loginToService(body.get("userMail"), body.get("password"));
    }

    @PostMapping("logout")
    public String logout() {
        httpSession.invalidate();
        return "logged out";
    }
}
