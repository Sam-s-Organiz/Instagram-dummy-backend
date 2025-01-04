package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.SearchRequestParameters;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import com.Instagram.Dummy.utils.AuthenticatedUserUtil;
import org.slf4j.ILoggerFactory;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTservice jwTservice;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    public AuthenticatedUserUtil authenticatedUserUtil;


    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public ResponseEntity<User> createUser(UserRequest userRequest) {
        System.out.println("Register UserRequest :" + userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setBio(userRequest.getBio());

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public UserDto login(UserRequest userRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User user = findUserByEmailOrThrow(userRequest.getEmail());
            return convertToUserDto(user, jwTservice.generateToken(userRequest.getEmail()));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
    }

    public ResponseEntity<String> updateProfilePhoto(Long id, String profilePhoto) {
        User user = findUserByIdOrThrow(id);
        user.setProfilePicture(profilePhoto);
        userRepository.save(user);
        return new ResponseEntity<>("Profile picture updated successfully", HttpStatus.OK);
    }


    public List<UserDto> searchUsers(String searchTerm, SearchRequestParameters searchRequestParameters) {
        Pageable pageRequest = PageRequest.of(
                searchRequestParameters.getPageNumber(),
                (int) searchRequestParameters.getPageSize()
        );

        Page<User> userPage = userRepository.findUsersByUsernameOrEmailContaining(searchTerm, pageRequest);

        Long currentUserId = AuthenticatedUserUtil.getAuthenticatedUser().getId();
        System.out.println("currentUserId"+currentUserId);

        List<Long> userIds = userPage.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Long> followedUserIds = followRepository.findFollowedUserIds(currentUserId, userIds);

        return userPage.stream()
                .map(user -> convertToUserDtoWithFollowStatus(user, followedUserIds))
                .collect(Collectors.toList());
    }


    private UserDto convertToUserDtoWithFollowStatus(User user, List<Long> followedUserIds) {
        UserDto userDto = convertToUserDto(user); // Reuse existing conversion logic
        userDto.setFollowed(followedUserIds.contains(user.getId())); // Set follow status
        return userDto;
    }


    private UserDto convertToUserDto(User user) {
        return convertToUserDto(user, null);
    }


    private UserDto convertToUserDto(User user, String jwtToken) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setProfilePicture(user.getProfilePicture());
        userDto.setBio(user.getBio());
        userDto.setJtwToken(jwtToken);
        return userDto;
    }
}
