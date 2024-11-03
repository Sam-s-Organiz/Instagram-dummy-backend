package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.services.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@CrossOrigin(origins = "*")
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    private LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<String> likeParticularPost(@PathVariable Long postId) {
        logger.info("User is liking post with ID: {}", postId);
         var likedPost = likeService.likePost(postId);

        if (likedPost != null) {
            return ResponseEntity.ok("Post liked successfully");
        } else {
            // Handle the case where the like action failed (e.g., post doesn't exist or already liked)
            return ResponseEntity.badRequest().body("Failed to like the post");
        }
    }
}
