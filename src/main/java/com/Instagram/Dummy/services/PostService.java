package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.pojo.PostImageResponse;
import com.Instagram.Dummy.repo.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public PostImageResponse createPost(User user, MultipartFile file, String imageUrl, String caption) {
        if (isUrlProvided(imageUrl)) {
            return savePostWithUrl(user, imageUrl, caption);
        } else if (isFileProvided(file)) {
            return savePostWithFileData(user, file, caption);
        } else {
            throw new RuntimeException("No valid file or URL provided.");
        }
    }

    private boolean isUrlProvided(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty();
    }

    private boolean isFileProvided(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private PostImageResponse savePostWithUrl(User user, String imageUrl, String caption) {
        Post post = createBasePost(user, caption, "URL");
        post.setImageUrl(imageUrl);

        Post savedPost = postRepository.save(post);
        return buildPostImageResponse(savedPost);
    }

    private PostImageResponse savePostWithFileData(User user, MultipartFile file, String caption) {
        try {
            byte[] fileData = file.getBytes();
            Post post = createBasePost(user, caption, "FILE");
            post.setFileData(fileData);

            Post savedPost = postRepository.save(post);
            return buildPostImageResponse(savedPost);
        } catch (IOException e) {
            log.error("Failed to save file data", e);
            throw new RuntimeException("File data saving failed: " + e.getMessage());
        }
    }

    private Post createBasePost(User user, String caption, String sourceType) {
        Post post = new Post();
        post.setUser(user);
        post.setCaption(caption != null ? caption : "");
        post.setSourceType(sourceType);
        return post;
    }

    private PostImageResponse buildPostImageResponse(Post post) {
        return new PostImageResponse(
                post.getId(),
                post.getImageUrl(),
                post.getCaption()
        );
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToPostDTO)
                .collect(Collectors.toList());
    }

    private PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setCaption(post.getCaption());

        if ("FILE".equals(post.getSourceType())) {
            postDTO.setFileData(post.getFileData());
            postDTO.setImageUrl(null);
        } else if ("URL".equals(post.getSourceType())) {
            postDTO.setImageUrl(post.getImageUrl());
            postDTO.setFileData(null);
        }

        return postDTO;
    }
}
