package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

import java.util.*;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable
    ) {

        ResultPaginationDTO arr = this.userService.findAll(spec, pageable);
        return ResponseEntity.ok().body(arr);
    }
    @PostMapping("users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        // ma hoa mat khau
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }


    @DeleteMapping("users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInValidException {
        if(id >= 1500) {
            throw new IdInValidException("Id phải nhỏ hơn 1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("users/{id}")
        public ResponseEntity<User> handleGetUserById(@PathVariable("id") long id) {

                User user = this.userService.handleUserById(id);
            return ResponseEntity.ok().body(user);
    }
    @PutMapping("users/{id}")
    public ResponseEntity<User> handleUpdateUser(@RequestBody User user, @PathVariable long id) {
        User updateUser = this.userService.handleUpdateUser(user, id);
        return ResponseEntity.ok().body(updateUser);
    }
}
