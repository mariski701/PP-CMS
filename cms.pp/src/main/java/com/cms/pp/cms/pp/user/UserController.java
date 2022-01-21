package com.cms.pp.cms.pp.user;

import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(
        origins = {"http://localhost:4200"},
        allowCredentials = "true",
        maxAge = 3600,
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,RequestMethod.POST,
                RequestMethod.DELETE, RequestMethod.PUT,
                RequestMethod.PATCH, RequestMethod.OPTIONS,
                RequestMethod.HEAD, RequestMethod.TRACE
        }
)
@RequestMapping("/api/user/")
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        java.util.Date date = new java.util.Date();
        System.out.println("["+date+"]"+"[USER]: " + username + " logged out from service");
        ErrorProvidedDataHandler errorProvidedDataHandler  = new ErrorProvidedDataHandler();
        errorProvidedDataHandler.setError("2001");
        httpSession.invalidate();
        return errorProvidedDataHandler;
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
        System.out.println(body.get("userName"));
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
