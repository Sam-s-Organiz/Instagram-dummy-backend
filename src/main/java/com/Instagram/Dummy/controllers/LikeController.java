package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.exceptions.PostNotFoundException;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.services.LikeService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
@CrossOrigin(origins = "*")
public class LikeController {

  private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

  @Autowired private LikeService likeService;

  @PutMapping("/post/{postId}")
  public ResponseEntity<String> likeParticularPost(@PathVariable Long postId) {
    logger.info("User is liking/disliking post with ID: {}", postId);
    try {
      String action = likeService.likeOrDislikePost(postId);
      return ResponseEntity.ok("Post " + action + " successfully");
    } catch (PostNotFoundException e) {
      logger.error("Post with ID {} not found", postId, e);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    } catch (Exception e) {
      logger.error("An error occurred while liking the post with ID {}", postId, e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }
  }

  @GetMapping("/allLikes")
  public ResponseEntity<List<PostDTO>> getPosts() {
    List<PostDTO> posts = likeService.getAllPostsWithLikes();
    return ResponseEntity.ok(posts);
  }

  //    @GetMapping("/post/{postId}")
  //    public ResponseEntity<?> getLikeCountForPost(@PathVariable Long postId) {
  //        logger.info("Fetching like count for post with ID: {}", postId);
  //        try {
  //            long likeCount = likeService.getLikeCountForPost(postId);
  //            return ResponseEntity.ok(likeCount);
  //        } catch (PostNotFoundException e) {
  //            logger.error("Post with ID {} not found", postId, e);
  //            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
  //        } catch (Exception e) {
  //            logger.error("An error occurred while fetching like count for post ID {}", postId,
  // e);
  //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
  // occurred");
  //        }
  //
  //    }

}
