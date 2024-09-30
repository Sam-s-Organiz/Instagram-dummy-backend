package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.services.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class FollowUserController {

    private static final Logger logger = LoggerFactory.getLogger(FollowUserController.class);

    @Autowired
    private FollowService followService;

    // Endpoint to follow another user
    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId, @RequestParam Long followerId) {
        logger.info("Follow request: User {} follows User {}", followerId, userId);
        followService.followUser(followerId, userId); // Call followUser method from FollowService
        return ResponseEntity.ok("User followed successfully");
    }

    @GetMapping("/feeds/{userId}")
    public ResponseEntity<?> getUserFeeds(@PathVariable Long userId) {
        logger.info("Fetching feed for User {}", userId);
        return ResponseEntity.ok(followService.getUserFeeds(userId));
    }
}
