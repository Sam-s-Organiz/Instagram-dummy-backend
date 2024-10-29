package com.Instagram.Dummy.pojo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostImageResponse {
    private Long id;
    private String imageUrl; // For the URL of the image
    private String caption;   // Corrected spelling from 'captian' to 'caption'
    private byte[] fileData;  // New field for binary image data

    public PostImageResponse(Long id, String imageUrl, String caption) {
    }
}
