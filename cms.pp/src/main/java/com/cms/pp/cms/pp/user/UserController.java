package com.cms.pp.cms.pp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user/")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("register")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping("delete/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    @GetMapping("getusers")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
