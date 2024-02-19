package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.CustomTopCommentersClass;
import com.cms.pp.cms.pp.model.dto.CMSUserDTO;
import com.cms.pp.cms.pp.model.dto.UserDTO;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.service.IUserService;
import com.cms.pp.cms.pp.service.MyUserDetailsService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/user/")
public class UserController {
    private final IUserService userService;
    private final HttpSession httpSession ;
    private final MyUserDetailsService myUserDetailsService;

    @PostMapping("register")
    public Object addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping("cms/register")
    public Object addCMSUser(@RequestBody CMSUserDTO cmsUserDTO) {
        return userService.addCMSUser(cmsUserDTO);
    }

    @DeleteMapping("delete/{id}")
    public Object deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @GetMapping("getusers")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("login")
    public Object login(@RequestBody Map<String, String> body ) {
        return userService.loginToService(body.get("userMail"), body.get("password"));
    }

    @PostMapping("logout")
    public Object logout() {
        return userService.logout();
    }

    @GetMapping("findbyid/{id}")
    public User findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @GetMapping("find/{userName}")
    public List<User> findByUserNameIgnoreCaseContaining(@PathVariable String userName) {
        return userService.findByUserNameIgnoreCaseContaining(userName);
    }

    @GetMapping("find/{page}/{size}/{username}")
    public List<UserDTO> findSomeUsersByLazyLoadingAndUserName(@PathVariable int page, @PathVariable int size, @PathVariable String username) {
        List<User> users = userService.findSomeUsersByLazyLoadingAndUserName(page, size, username);
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : users) {
            userDTOList.add(new UserDTO(user.getId(), user.getUserName(), user.getUserMail()));
        }
        return userDTOList;
    }

    @PutMapping("edit/username")
    public Object editUserName(@RequestBody Map<String, String> body) {
        return userService.editUserName(body.get("userName"));
    }

    @PutMapping("edit/password")
    public Object changePassword(@RequestBody Map<String, String> body) {
        return userService.changePassword(body.get("oldPassword"), body.get("newPassword"));
    }

    @PutMapping("edit/mail")
    public Object editUserMail(@RequestBody Map<String, String> body) {
        return userService.editUserMail(body.get("newMail"));
    }

    @PutMapping("edit/role")
    public Object editUserRole(@RequestBody Map<String, String> body) {
        return userService.editUserRole(body.get("roleName"), Integer.parseInt(body.get("userID")));
    }

    @GetMapping("cms/users")
    public List<User> findCmsUsers() {
        return userService.findCmsUsers();
    }

    @PutMapping("edit/changeMail")
    public Object changeUserMail(@RequestBody Map<String, String> body) {
        return userService.changeUserMail(Integer.parseInt(body.get("userId")), body.get("userMail"));
    }

    @PutMapping("edit/changeNickname")
    public Object changeUserNickname(@RequestBody Map<String, String> body) {
        return userService.changeUserName(Integer.parseInt(body.get("userId")), body.get("userName"));
    }

    @GetMapping("find/topCommenter/{size}")
    public List<CustomTopCommentersClass> findTheBestCommenter(@PathVariable int size) {
        return userService.findTheBestCommenter(size);
    }
}
