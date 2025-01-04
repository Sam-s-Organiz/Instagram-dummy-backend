package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.services.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class FollowUserController {

    private static final Logger logger = LoggerFactory.getLogger(FollowUserController.class);

    @Autowired
    private FollowService followService;

    @PostMapping("/follow/{followerId}")
    public ResponseEntity<String> followUser(@PathVariable Long followerId) {
        logger.info("Follow request: follows User {}", followerId);
        followService.followUser(followerId);
        return ResponseEntity.ok("User followed successfully");
    }

    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long followingId) {
        logger.info("Unfollow request: unfollows User {}", followingId);
        followService.unfollowUser(followingId); // Call unfollowUser method from FollowService
        return ResponseEntity.ok("User unfollowed successfully");
    }

    @GetMapping("/follow/count/{userId}")
    public ResponseEntity<Map<String, Integer>> getFollowCounts(@PathVariable Long userId) {
        logger.info("Fetching follower and following count for User {}", userId);
        Map<String, Integer> followCounts = followService.getFollowCounts(userId);
        return ResponseEntity.ok(followCounts);
    }
}
