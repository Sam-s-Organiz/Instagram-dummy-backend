package com.Instagram.Dummy.mapper;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserDto toDto(User user) {
    return toDto(user, null);
  }

  public UserDto toDto(User user, String jwtToken) {
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setEmail(user.getEmail());
    dto.setBio(user.getBio());
    dto.setProfilePicture(user.getProfilePicture());
    dto.setJwtToken(jwtToken);
    return dto;
  }
}
