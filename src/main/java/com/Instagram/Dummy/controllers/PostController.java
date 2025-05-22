package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.services.PostService;
import com.Instagram.Dummy.services.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

  @Autowired private PostService postService;

  @Autowired private UserService userService;
  private static final Logger logger = LoggerFactory.getLogger(PostController.class);

  @PostMapping("/upload")
  public ResponseEntity<String> uploadPost(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam(value = "imageUrl", required = false) String imageUrl,
      @RequestParam(value = "caption", required = false) String caption) {

    if ((file == null || file.isEmpty()) && (imageUrl == null || imageUrl.trim().isEmpty())) {
      return ResponseEntity.badRequest()
          .body("You must provide either an image file or an image URL.");
    }

    try {
      postService.createPost(file, imageUrl, caption);
      return ResponseEntity.ok("Post uploaded successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to upload post: " + e.getMessage());
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
    List<PostDTO> response = postService.getPostsByUser(userId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/followed")
  public List<PostDTO> getFollowedPosts() {
    return postService.getPostsOfFollowedUsersAndSelf();
  }

  @GetMapping("/user/feeds")
  public List<PostDTO> getUserFeeds() {
    return postService.getFeedPosts();
  }
}
