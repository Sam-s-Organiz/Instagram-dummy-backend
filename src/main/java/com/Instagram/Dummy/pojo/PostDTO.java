package com.Instagram.Dummy.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor // Remove this if you have a constructor defined
@ToString
public class PostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String caption;
    private String imageUrl;

    // Getters and Setters
}
