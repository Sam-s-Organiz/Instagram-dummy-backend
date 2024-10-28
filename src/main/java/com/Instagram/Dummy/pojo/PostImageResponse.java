package com.Instagram.Dummy.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostImageResponse {
    private Long id;
    private String imageUrl;
    private String captian;

    public PostImageResponse(Long id, String imageUrl, String captian) {
    }
}
