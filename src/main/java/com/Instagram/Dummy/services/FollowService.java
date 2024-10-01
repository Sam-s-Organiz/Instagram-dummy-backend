package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;

    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        // Check if the follower is trying to follow themselves
        if (followerId.equals(followingId)) {
            throw new RuntimeException("A user cannot follow themselves");
        }

        // Check if the follower already follows the following user
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RuntimeException("User is already following this account");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }


    public List<User> getUserFeeds(Long userId) {
        // Logic to get feeds based on followed users
        // This might involve fetching the followers of the user and then
        // collecting posts from those users.
        return null; // Replace with actual logic
    }
}
