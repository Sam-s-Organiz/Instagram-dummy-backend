package com.Instagram.Dummy.services;

import com.Instagram.Dummy.mapper.PostMapper;
import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.PostDTO;
import com.Instagram.Dummy.repo.LikeRepository;
import com.Instagram.Dummy.repo.PostRepository;
import com.Instagram.Dummy.utils.AuthenticatedUserUtil;
import com.Instagram.Dummy.utils.ImageUtils;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final LikeRepository likeRepository;
  private final FollowService followService;
  private final AuthenticatedUserUtil authenticatedUserUtil;
  private final PostMapper postMapper;

  public void createPost(MultipartFile file, String imageUrl, String caption) {
    User user = authenticatedUserUtil.getAuthenticatedUser();

    if (!isValidInput(imageUrl, file)) {
      throw new IllegalArgumentException("Either an image file or an image URL must be provided.");
    }

    Post post = buildPost(user, caption, imageUrl, file);
    postRepository.save(post);
  }

  public List<PostDTO> getPostsByUser(Long userId) {
    return postRepository.findByUserId(userId).stream()
            .map(post -> postMapper.toDto(post, likeRepository.countByPostId(post.getId())))
            .collect(Collectors.toList());
  }

  public List<PostDTO> getFeedPosts() {
    Long userId = authenticatedUserUtil.getAuthenticatedUser().getId();
    List<Long> userIds = followService.getFollowedUserIdsIncludingSelf(userId);

    log.info("Fetching posts for feed. User ID: {}, Post owners: {}", userId, userIds);

    return postRepository.findByUserIdIn(userIds).stream()
            .map(post -> postMapper.toDto(post, likeRepository.countByPostId(post.getId())))
            .collect(Collectors.toList());
  }

  private boolean isValidInput(String imageUrl, MultipartFile file) {
    return (imageUrl != null && !imageUrl.isEmpty()) || (file != null && !file.isEmpty());
  }

  private Post buildPost(User user, String caption, String imageUrl, MultipartFile file) {
    Post.PostBuilder builder = Post.builder().user(user).caption(caption != null ? caption : "");

    if (imageUrl != null && !imageUrl.isEmpty()) {
      builder.sourceType("URL");
      builder.imageUrl(imageUrl.trim());
    } else if (file != null && !file.isEmpty()) {
      try {
        String savedPath =
                ImageUtils.saveImageToLocal(file, user.getId()); // e.g., "/images/user_5/photo.jpg"
        builder.sourceType("FILE");
        builder.imageUrl(savedPath);
      } catch (IOException e) {
        throw new RuntimeException("Image saving failed", e);
      }
    } else {
      throw new IllegalArgumentException("No valid file or URL provided.");
    }

    return builder.build();
  }

  public List<PostDTO> getPostsOfFollowedUsersAndSelf() {
    Long userId = authenticatedUserUtil.getAuthenticatedUser().getId();

    List<Long> followingIds = followService.getFollowedUserIdsIncludingSelf(userId);

    return postRepository.findByUserIdIn(followingIds).stream()
            .map(post -> postMapper.toDto(post, likeRepository.countByPostId(post.getId())))
            .collect(Collectors.toList());
  }
}
