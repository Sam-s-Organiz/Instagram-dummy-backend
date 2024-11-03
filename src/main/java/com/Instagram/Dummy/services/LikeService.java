package com.Instagram.Dummy.services;

import com.Instagram.Dummy.exceptions.PostNotFoundException;
import com.Instagram.Dummy.exceptions.UserAlreadyLikedPostException;
import com.Instagram.Dummy.modals.Like;
import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.LikeRepository;
import com.Instagram.Dummy.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    public Like likePost(Long postId) {
        // Extract the user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal(); // Assuming you're using Spring Security

        // Check if the post exists
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // Check if the user has already liked this post
            if (!likeRepository.existsByUserIdAndPostId(user.getId(), postId)) {
                Like like = new Like();
                like.setPost(post);
                like.setUser(user);
                return likeRepository.save(like);
            } else {
                // Handle the case where the user has already liked this post
                throw new UserAlreadyLikedPostException("User has already liked this post.");
            }
        } else {
             throw new PostNotFoundException(postId);
        }
    }

    // Method to get all posts with like counts
    public List<Post> getAllPostsWithLikes() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> post.setLikes(likeRepository.findByPostId(post.getId()))); // Set likes for each post
        return posts;
    }
}

