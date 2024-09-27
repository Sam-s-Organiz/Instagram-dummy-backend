package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.services.UserService;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addNewUser(@RequestBody UserRequest userRequest) {
        logger.info("User request received: {}", userRequest);
        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(user); // 200 OK
    }

}
