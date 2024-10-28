package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.pojo.PostImageResponse;
import com.Instagram.Dummy.services.PostService;
import com.Instagram.Dummy.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")

public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);


    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadPost(
            @PathVariable Long userId,
            @RequestParam(required = false) MultipartFile file, // Use MultipartFile here
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) String caption) {

        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Call the service to create the post
            PostImageResponse response = postService.createPost(user, file, imageUrl, caption);
            return ResponseEntity.ok("Image uploaded successfully: " + response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }






    // Get all posts for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsForUser(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }
}
