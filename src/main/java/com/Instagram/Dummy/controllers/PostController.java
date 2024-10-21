package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.services.PostService;
import com.Instagram.Dummy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")

public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    // Upload a new post
    @PostMapping("/upload")
    public ResponseEntity<Post> uploadPost(@RequestParam Long userId,
                                           @RequestParam String imageUrl,
                                           @RequestParam(required = false) String caption) {
        User user = userService.getUserById(userId);
        Post post = postService.createPost(user, imageUrl, caption);
        return ResponseEntity.ok(post);
    }

    // Get all posts for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsForUser(@PathVariable Long userId) {
        List<PostDTO> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }
}
