package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.services.PostService;
import com.Instagram.Dummy.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(required = false) String caption) {

        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            boolean isFileProvided = file != null && !file.isEmpty();
            boolean isImageUrlProvided = imageUrl != null && !imageUrl.isEmpty();
            if (!isFileProvided && !isImageUrlProvided) {
                return ResponseEntity.badRequest().body("Either file or imageUrl must be provided.");
            }
            if (isFileProvided && isImageUrlProvided) {
                return ResponseEntity.badRequest().body("Provide either file or imageUrl, not both.");
            }
            postService.createPost(user, file, imageUrl, caption);
            return ResponseEntity.ok("Image uploaded successfully: " );

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        List<PostDTO> response = postService.getPostsByUser(userId);
        return ResponseEntity.ok(response);

    }
}
