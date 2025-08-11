package com.Instagram.Dummy.services;

import com.Instagram.Dummy.mapper.UserMapper;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.SearchRequestParameters;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import com.Instagram.Dummy.utils.AuthenticatedUserUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JWTservice jwTservice;
  @Autowired private FollowRepository followRepository;
  @Autowired private AuthenticatedUserUtil authenticatedUserUtil;
  @Autowired private UserMapper userMapper;
  @Autowired private TenantService tenantService;

  public ResponseEntity<User> createUser(UserRequest userRequest, String tenantId) {
    System.out.println("Register UserRequest: " + userRequest);
    validateAndGetTenantId(tenantId);

    User user = new User();
    user.setUsername(userRequest.getUsername());
    user.setEmail(userRequest.getEmail());
    user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    user.setBio(userRequest.getBio());

    User savedUser = userRepository.save(user);
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }

  public UserDto login(UserRequest userRequest, String tenantId) {
    validateAndGetTenantId(tenantId);
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userRequest.getEmail(), userRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      User user = findUserByEmailOrThrow(userRequest.getEmail());
      String token = jwTservice.generateToken(userRequest.getEmail());
      return userMapper.toDto(user, token);
    }

    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
  }

  public ResponseEntity<String> updateProfilePhoto(String profilePhoto) {
    Long id = authenticatedUserUtil.getAuthenticatedUser().getId();
    User user = findUserByIdOrThrow(id);
    user.setProfilePicture(profilePhoto);
    userRepository.save(user);
    return ResponseEntity.ok("Profile picture updated successfully");
  }

  public List<UserDto> searchUsers(String searchTerm, SearchRequestParameters params) {
    Pageable pageRequest = PageRequest.of(params.getPageNumber(), (int) params.getPageSize());

    Page<User> userPage =
        userRepository.findUsersByUsernameOrEmailContaining(searchTerm, pageRequest);
    Long currentUserId = authenticatedUserUtil.getAuthenticatedUser().getId();

    List<Long> userIds = userPage.stream().map(User::getId).collect(Collectors.toList());
    List<Long> followedUserIds = followRepository.findFollowedUserIds(currentUserId, userIds);

    return userPage.stream()
        .map(
            user -> {
              UserDto dto = userMapper.toDto(user);
              dto.setFollowed(followedUserIds.contains(user.getId()));
              return dto;
            })
        .collect(Collectors.toList());
  }

  public Optional<User> getUserById(Long userId) {
    return userRepository.findById(userId);
  }

  public User findUserByIdOrThrow(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private User findUserByEmailOrThrow(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }

  private String validateAndGetTenantId(String tenantId) {
    if (tenantId == null) {
      tenantId = "default";
    }
    // Assuming getTenantById throws ResponseStatusException if tenant doesn't exist
    tenantService.getTenantById(tenantId);
    return tenantId;
  }
}
