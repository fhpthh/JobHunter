package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {

        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);
        Pageable pageable =  PageRequest.of(current - 1, pageSize);

        ResultPaginationDTO arr = this.userService.findAll(pageable);
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
