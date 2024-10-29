package com.Instagram.Dummy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String caption;
    private String imageUrl; // For URL type images
    private byte[] fileData; // For file type images

}

