package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Create a new post
    public Post createPost(User user, String imageUrl, String caption) {
        Post post = new Post();
        post.setUser(user);
        post.setImageUrl(imageUrl);
        post.setCaption(caption);
        return postRepository.save(post);
    }

    // Get all posts for a specific user
    public List<PostDTO> getPostsByUser(Long userId) {
        // Fetch posts from the repository
        List<Post> posts = postRepository.findByUserId(userId);

        // Map Post entities to PostDTO
        return posts.stream()
                .map(post -> {
                    PostDTO postDTO = new PostDTO();
                    postDTO.setId(post.getId());
                    postDTO.setUserId(post.getUser().getId());
                    postDTO.setUsername(post.getUser().getUsername());
                    postDTO.setCaption(post.getCaption());
                    postDTO.setImageUrl(post.getImageUrl());
                    return postDTO;
                })
                .collect(Collectors.toList());
    }
}
