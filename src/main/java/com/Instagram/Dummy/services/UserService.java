package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserDto;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTservice jwTservice;

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserDto getUserById(Long userId) {
        User user = findUserByIdOrThrow(userId);

        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setBio(user.getBio());
        return userDTO;
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
        System.out.println("Login UserRequest :" + userRequest);
//        User user = findUserByEmailOrThrow(userRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
        // Check if the password matches
        if (authentication.isAuthenticated()) {
            UserDto use = new UserDto();
            use.setJtwToken(jwTservice.generateToken(userRequest.getEmail()));
            return use;
        }

//        if (!user.getPassword().equals(userRequest.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
//        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
    }

    public ResponseEntity<String> updateProfilePhoto(Long id, String profilePhoto) {
        User user = findUserByIdOrThrow(id);

        user.setProfilePicture(profilePhoto);
        userRepository.save(user);
        return new ResponseEntity<>("Profile picture updated successfully", HttpStatus.OK);
    }

}
