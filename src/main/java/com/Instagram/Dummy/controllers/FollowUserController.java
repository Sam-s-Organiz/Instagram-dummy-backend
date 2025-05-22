package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.services.FollowService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin(origins = "*")
public class FollowUserController {

  private static final Logger logger = LoggerFactory.getLogger(FollowUserController.class);

  @Autowired private FollowService followService;

  @PostMapping("/{targetUserId}")
  public ResponseEntity<String> followUser(@PathVariable Long targetUserId) {
    logger.info("Follow request to User {}", targetUserId);
    followService.followUser(targetUserId);
    return ResponseEntity.ok("User followed successfully");
  }

  @DeleteMapping("/{targetUserId}")
  public ResponseEntity<String> unfollowUser(@PathVariable Long targetUserId) {
    logger.info("Unfollow request to User {}", targetUserId);
    followService.unfollowUser(targetUserId);
    return ResponseEntity.ok("User unfollowed successfully");
  }

  @GetMapping("/counts/{userId}")
  public ResponseEntity<Map<String, Integer>> getFollowCounts(@PathVariable Long userId) {
    logger.info("Fetching follow counts for User {}", userId);
    Map<String, Integer> followCounts = followService.getFollowCounts(userId);
    return ResponseEntity.ok(followCounts);
  }
}
