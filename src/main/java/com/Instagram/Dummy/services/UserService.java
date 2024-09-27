package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.pojo.UserRequest;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<User> createUser(UserRequest userRequest) {
        System.out.println("UserRequestsss :" + userRequest);
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setProfilePicture(userRequest.getProfilePicture());
        user.setBio(userRequest.getBio());
        System.out.println("userrrrr :" + user);

        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElse(null);  // Return null if the user is not found
    }
}
