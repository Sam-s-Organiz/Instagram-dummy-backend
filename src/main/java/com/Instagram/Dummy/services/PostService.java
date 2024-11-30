package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.LikeRepository;
import com.Instagram.Dummy.repo.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private FollowRepository followRepository;

    @Async
    public void createPost(User user, MultipartFile file, String imageUrl, String caption) {
        if (!isValidInput(imageUrl, file)) {
            throw new RuntimeException("No valid file or URL provided.");
        }

        if (isUrlProvided(imageUrl)) {
            savePostWithUrl(user, imageUrl, caption);
        } else {
            savePostWithFileData(user, file, caption);
        }
    }

    private boolean isValidInput(String imageUrl, MultipartFile file) {
        return (imageUrl != null && !imageUrl.isEmpty()) || (file != null && !file.isEmpty());
    }

    private boolean isUrlProvided(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty();
    }

    private void savePostWithUrl(User user, String imageUrl, String caption) {
        savePost(user, caption, "URL", imageUrl, null);
    }

    @Async
    private void savePostWithFileData(User user, MultipartFile file, String caption) {
        try {
            byte[] resizedFileData = resizeImage(file, 800, 600);

            savePost(user, caption, "FILE", null, resizedFileData);
        } catch (IOException e) {
            log.error("Failed to save resized file data", e);
            throw new RuntimeException("File data saving failed: " + e.getMessage());
        }
    }

    private void savePost(User user, String caption, String sourceType, String imageUrl, byte[] fileData) {
        Post post = createBasePost(user, caption, sourceType);
        post.setImageUrl(imageUrl);
        post.setFileData(fileData);
        postRepository.save(post);
    }

    private Post createBasePost(User user, String caption, String sourceType) {
        return Post.builder()
                .user(user)
                .caption(caption != null ? caption : "")
                .sourceType(sourceType)
                .build();
    }

    public List<PostDTO> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(post -> {
                    int likeCount = likeRepository.countByPostId(post.getId());
                    PostDTO postDTO = convertToPostDTO(post);
                    postDTO.setLikeCount(likeCount);
                    return postDTO;
                })
                .collect(Collectors.toList());
    }

    private PostDTO convertToPostDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername()) // Ensure this is correct
                .caption(post.getCaption())
                .imageUrl("URL".equals(post.getSourceType()) ? post.getImageUrl() : null)
                .fileData("FILE".equals(post.getSourceType()) ? post.getFileData() : null)
                .build();
    }


    // Method to resize the image to the desired dimensions (width and height)
    private byte[] resizeImage(MultipartFile file, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        // Scale the image to the desired dimensions (with smooth scaling)
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create a new BufferedImage to hold the resized image
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        // Convert the resized image to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "JPEG", outputStream);  // Save as JPEG for better compression
        return outputStream.toByteArray();
    }

    public List<PostDTO> getPostsOfFollowedUsers(User loggedInUser) {
        // Fetch the IDs of users followed by the logged-in user
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(loggedInUser.getId());

        // Fetch posts of the followed users
        return postRepository.findByUserIdIn(followingIds).stream()
                .map(post -> {
                    int likeCount = likeRepository.countByPostId(post.getId());
                    PostDTO postDTO = convertToPostDTO(post);
                    postDTO.setLikeCount(likeCount); // Add like count
                    return postDTO;
                })
                .collect(Collectors.toList());
    }


}


