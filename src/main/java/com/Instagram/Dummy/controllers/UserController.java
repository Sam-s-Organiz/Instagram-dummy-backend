package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.SearchRequestParameters;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
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

  @Autowired private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<User> signUp(@RequestBody UserRequest userRequest) {
    logger.info("SignUp request received: {}", userRequest);
    return userService.createUser(userRequest);
  }

  @PostMapping("/login")
  public UserDto signIn(@RequestBody UserRequest userRequest) {
    logger.info("SignIn request received: {}", userRequest);
    return userService.login(userRequest);
  }

  @PatchMapping("/profilepic")
  public ResponseEntity<String> uploadProfilePic(@RequestParam String profilePhoto) {
    logger.info("Inside uploadProfilePic with new profilePhoto: {}", profilePhoto);
    return userService.updateProfilePhoto(profilePhoto); // no userId passed here
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userService.findUserByIdOrThrow(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/search")
  public ResponseEntity<List<UserDto>> searchUsers(
      @RequestParam String term,
      @Valid @ModelAttribute SearchRequestParameters searchRequestParameters) {

    List<UserDto> fetchedUsers = userService.searchUsers(term, searchRequestParameters);

    if (fetchedUsers.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(fetchedUsers);
  }
}
