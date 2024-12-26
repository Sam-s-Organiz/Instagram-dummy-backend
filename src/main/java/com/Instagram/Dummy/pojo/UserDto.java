package com.Instagram.Dummy.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String jtwToken;
}
