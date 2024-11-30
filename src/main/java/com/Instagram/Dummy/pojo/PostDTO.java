package com.Instagram.Dummy.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String caption;
    private String imageUrl;
    private byte[] fileData;
    private int likeCount;

}

