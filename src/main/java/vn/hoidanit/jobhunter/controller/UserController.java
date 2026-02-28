package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long user_id) throws IdInvalidException {
        if (user_id > 1500) {
            throw new IdInvalidException("Id is not higher than 1500");
        }

        this.userService.handleDeleteUser(user_id);
        return ResponseEntity.ok("Deleted user_id" + user_id);
        // return ResponseEntity.status(HttpStatus.OK).body("Deleted user with id " +
        // user_id);
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long user_id) {
        User result = this.userService.fetchUserById(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // fetch all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> result = this.userService.fetchAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // update
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User result = this.userService.handleUpdatUser(user);
        return ResponseEntity.ok(result);
    }

}
