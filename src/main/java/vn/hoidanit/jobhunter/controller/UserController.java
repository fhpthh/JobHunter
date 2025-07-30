package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getUsers() {
        List<User> arr = this.userService.findAll();
        return ResponseEntity.ok().body(arr);
    }
    @PostMapping("users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User createUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }
    @DeleteMapping("users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build() ;
    }

    @GetMapping("users/{id}")
        public ResponseEntity<User> handleGetUserById(@PathVariable long id) {
                User user = this.userService.handleUserById(id);
            return ResponseEntity.ok().body(user);
    }
    @PutMapping("users/{id}")
    public ResponseEntity<User> handleUpdateUser(@RequestBody User user, @PathVariable long id) {
        User updateUser = this.userService.handleUpdateUser(user, id);
        return ResponseEntity.ok().body(updateUser);
    }
}
