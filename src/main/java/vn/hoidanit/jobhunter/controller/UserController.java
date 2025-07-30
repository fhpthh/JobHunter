package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("users")
    public List<User> getUsers() {
        List<User> arr = this.userService.findAll();
        return arr;
    }
    @PostMapping("users")
    public User createNewUser(@RequestBody User user) {
        User createUser = this.userService.handleCreateUser(user);
        return createUser;
    }

}
