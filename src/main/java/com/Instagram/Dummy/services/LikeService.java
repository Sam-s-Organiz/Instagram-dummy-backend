package com.Instagram.Dummy.services;

import com.Instagram.Dummy.config.JwtUserDetails;
import com.Instagram.Dummy.exceptions.PostNotFoundException;
import com.Instagram.Dummy.modals.Like;
import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.repo.LikeRepository;
import com.Instagram.Dummy.repo.PostRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

  @Autowired private PostRepository postRepository;

  @Autowired private LikeRepository likeRepository;

  @CacheEvict(value = "postsWithLikes", key = "#postId")
  public String likeOrDislikePost(Long postId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
    User user = jwtUserDetails.getUser();

    Optional<Post> optionalPost = postRepository.findById(postId);
    if (optionalPost.isEmpty()) {
      throw new PostNotFoundException("Post with ID " + postId + " not found");
    }

    // Check if the like already exists
    Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

    if (existingLike.isPresent()) {
      likeRepository.delete(existingLike.get());
      return "disliked";
    } else {
      Like like = new Like();
      like.setPost(optionalPost.get());
      like.setUser(user);
      likeRepository.save(like);
      return "liked";
    }
  }

  @Cacheable(value = "postsWithLikes", key = "#root.methodName")
  public List<PostDTO> getAllPostsWithLikes() {
    List<Post> posts = postRepository.findAll();
    return posts.stream()
        .map(
            post ->
                PostDTO.builder()
                    .id(post.getId())
                    .userId(post.getUser().getId())
                    .username(post.getUser().getUsername())
                    .imageUrl(post.getImageUrl())
                    .caption(post.getCaption())
                    .build())
        .toList();
  }
}
