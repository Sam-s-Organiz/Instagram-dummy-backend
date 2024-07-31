package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> addNewUser(@RequestBody UserRequest userRequest) {
        System.out.println("Requeeeeest :"+userRequest);
        return userService.createUser(userRequest);
    }
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}
