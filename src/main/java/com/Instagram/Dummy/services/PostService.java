package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.pojo.PostImageResponse;
import com.Instagram.Dummy.repo.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostImageResponse createPost(User user, MultipartFile file, String imageUrl, String caption) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // If imageUrl is provided, save the post with URL as source
            return savePost(user, imageUrl, caption, "URL");
        } else if (file != null && !file.isEmpty()) {
            // Handle file upload case
            String savedFileUrl = saveFile(file);  // Save the file and get the file URL
            return savePost(user, savedFileUrl, caption, "FILE");
        } else {
            throw new RuntimeException("No valid file or URL provided.");
        }
    }

    private PostImageResponse savePost(User user, String fileUrl, String caption, String sourceType) {
        Post post = new Post();
        post.setUser(user);
        post.setImageUrl(fileUrl);
        post.setCaption(caption != null ? caption : "");
        post.setSourceType(sourceType);

        // Save the post and convert it to PostImageResponse
        Post savedPost = postRepository.save(post);
        return new PostImageResponse(savedPost.getId(), savedPost.getImageUrl(), caption);
    }

    // Method to handle file saving and return the URL
    private String saveFile(MultipartFile file) {
        try {
            // Define where to save the file
            Path path = Paths.get("/actual/server/path/" + file.getOriginalFilename());
            File destinationFile = path.toFile();
            file.transferTo(destinationFile);  // Save the file

            // Return the URL of the saved file
            return "http://example.com/files/" + file.getOriginalFilename(); // Update with actual file URL
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw new RuntimeException("File saving failed: " + e.getMessage());
        }
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
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
