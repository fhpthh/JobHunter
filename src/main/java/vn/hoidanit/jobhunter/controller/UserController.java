package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @DeleteMapping("users/{id}")
    public String deleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return "deleted" ;
    }

    @GetMapping("users/{id}")
        public User handleGetUserById(@PathVariable long id) {
            return this.userService.handleUserById(id);
    }
    @PutMapping("users/{id}")
    public User handleUpdateUser(@RequestBody User user, @PathVariable long id) {
        User updateUser = this.userService.handleUpdateUser(user, id);
        return updateUser;
    }
}
