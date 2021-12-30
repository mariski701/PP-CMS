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
    public Object addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("cms/register")
    public Object addCMSUser(@RequestBody CMSUserDTO cmsUserDTO) {
        return userService.addCMSUser(cmsUserDTO);
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
    public Object login(@RequestBody Map<String, String> body ) {
        return userService.loginToService(body.get("userMail"), body.get("password"));
    }

    @PostMapping("logout")
    public String logout() {
        httpSession.invalidate();
        return "logged out";
    }

    @GetMapping("find/{userName}")
    public List<User> findByUserNameIgnoreCaseContaining(@PathVariable String userName) {
        return userService.findByUserNameIgnoreCaseContaining(userName);
    }

    @PutMapping("edit/username")
    public int editUserName(@RequestBody Map<String, String> body) {
        System.out.println(body.get("userName"));
        return userService.editUserName(body.get("userName"));
    }

    @PutMapping("edit/password")
    public int changePassword(@RequestBody Map<String, String> body) {
        return userService.changePassword(body.get("oldPassword"), body.get("newPassword"));
    }

    @PutMapping("edit/mail")
    public int editUserMail(@RequestBody Map<String, String> body) {
        return userService.editUserMail(body.get("newMail"));
    }

    @PutMapping("edit/role")
    public int editUserRole(@RequestBody Map<String, String> body) {
        return userService.editUserRole(body.get("roleName"), Integer.parseInt(body.get("userID")));
    }

    @GetMapping("cms/users")
    public List<User> findCmsUsers() {
        return userService.findCmsUsers();
    }

    @PutMapping("edit/changeMail")
    public int changeUserMail(@RequestBody Map<String, String> body) {
        return userService.changeUserMail(Integer.parseInt(body.get("userId")), body.get("userMail"));
    }

    @PutMapping("edit/changeNickname")
    public int changeUserNickname(@RequestBody Map<String, String> body) {
        return userService.changeUserName(Integer.parseInt(body.get("userId")), body.get("userName"));
    }
}
