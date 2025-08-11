package com.Instagram.Dummy.mapper;

import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.pojo.PostDTO;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

  private static final String BASE_URL = "http://localhost:8081"; // Match your Spring Boot server

  public PostDTO toDto(Post post, int likeCount) {
    String imageUrl;

    if ("URL".equals(post.getSourceType())) {
      imageUrl = post.getImageUrl();
    } else if ("FILE".equals(post.getSourceType())) {
      imageUrl = BASE_URL + post.getImageUrl(); // prepend base path for static files
    } else {
      imageUrl = null;
    }

    return PostDTO.builder()
        .id(post.getId())
        .userId(post.getUser().getId())
        .username(post.getUser().getUsername())
        .caption(post.getCaption())
        .imageUrl(imageUrl)
        .likeCount(likeCount)
        .build();
  }
}
