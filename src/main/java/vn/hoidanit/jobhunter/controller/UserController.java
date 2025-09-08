package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInValidException;

@RestController
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getUsers(
            @Filter Specification<User> spec,
            Pageable pageable
    ) {

        ResultPaginationDTO arr = this.userService.findAll(spec, pageable);
        return ResponseEntity.ok().body(arr);
    }
    @PostMapping("users")
    @ApiMessage("create a user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInValidException {

        boolean isEmail = this.userService.checkEmail(user.getEmail());
        if (isEmail) {
            throw new IdInValidException(
                    "Email " + user.getEmail() + "da ton tai, vui long su dung email khac"
            );
        }
        // ma hoa mat khau
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertTokenCreateUserDTO(createUser));
//       return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }


    @DeleteMapping("users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInValidException {
        User curuser = this.userService.handleUserById(id);
        if (curuser == null) {
            throw new IdInValidException("User witd id" + id + "not found");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("users/{id}")
    @ApiMessage("fetc user by id")
        public ResponseEntity<ResUserDTO> handleGetUserById(@PathVariable("id") long id) throws IdInValidException {

        User user = this.userService.handleUserById(id);
        if (user == null) {
            throw  new IdInValidException("User with id " + id + "not found");
        }
            return ResponseEntity.ok().body(this.userService.convertUserDTO(user));
    }
    @PutMapping("users")
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> handleUpdateUser(@RequestBody User user)  throws  IdInValidException {

        User updateUser = this.userService.handleUpdateUser(user);
        if (updateUser == null) {
            throw new IdInValidException("User with id " + user.getId() + "not found");
        }
        return ResponseEntity.ok().body(this.userService.convertToResUpdateUserDTO(user));
    }
}
