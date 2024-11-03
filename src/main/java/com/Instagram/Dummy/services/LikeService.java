package com.Instagram.Dummy.services;

import com.Instagram.Dummy.config.JwtUserDetails;
import com.Instagram.Dummy.exceptions.PostNotFoundException;
import com.Instagram.Dummy.exceptions.UserAlreadyLikedPostException;
import com.Instagram.Dummy.modals.Like;
import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.LikeRepository;
import com.Instagram.Dummy.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "postsWithLikes", key = "#postId")
    public Like likePost(Long postId) {
        // Extract the user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal(); // Cast to JwtUserDetails

        // Access the User entity from JwtUserDetails
        User user = jwtUserDetails.getUser(); // Get the User instance

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
                // Handle the case where the user has already liked the post
                throw new UserAlreadyLikedPostException("User has already liked this post.\"" + user.getId() + "  " + postId);
            }
        } else {
            throw new PostNotFoundException(postId);
        }
    }


    @Cacheable(value = "postsWithLikes", key = "#postId")
    public List<Post> getAllPostsWithLikes() {
        List<Post> posts = postRepository.findAll();
        posts.forEach(post -> post.setLikes(likeRepository.findByPostId(post.getId())));
        return posts;
    }
}

