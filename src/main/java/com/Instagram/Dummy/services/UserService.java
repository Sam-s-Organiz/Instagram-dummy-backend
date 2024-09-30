package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public ResponseEntity<User> createUser(UserRequest userRequest) {
        System.out.println("Register UserRequest :" + userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setBio(userRequest.getBio());

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public ResponseEntity<User> login(UserRequest userRequest) {
        System.out.println("Login UserRequest :" + userRequest);

        User user = findUserByEmailOrThrow(userRequest.getEmail());

        // Check if the password matches
        if (!user.getPassword().equals(userRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<String> updateProfilePhoto(Long id, String profilePhoto) {
        User user = findUserByIdOrThrow(id);

        user.setProfilePicture(profilePhoto);
        userRepository.save(user);
        return new ResponseEntity<>("Profile picture updated successfully", HttpStatus.OK);
    }

}
