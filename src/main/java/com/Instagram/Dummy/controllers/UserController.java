package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> SignUp(@RequestBody UserRequest userRequest) {
        logger.info("SignUp request received: {}", userRequest);
        return userService.createUser(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<User> signIn(@RequestBody UserRequest userRequest) {
        logger.info("SignIn request received: {}", userRequest);
        return userService.login(userRequest);
    }

    @PatchMapping("/profilepic")
    public ResponseEntity<String> uploadProfilePic(
            @RequestParam Long userId,
            @RequestParam String profilePhoto
    ) {
        logger.info("Inside uploadProfilePic function: {}", profilePhoto);
        return userService.updateProfilePhoto(userId, profilePhoto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserByIdOrThrow(id);
        return ResponseEntity.ok(user);
    }

}
