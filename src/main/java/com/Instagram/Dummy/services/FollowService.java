package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public Map<String, Integer> getFollowCounts(Long userId) {
        int followersCount = followRepository.countFollowers(userId);
        int followingCount = followRepository.countFollowing(userId);

        Map<String, Integer> followCounts = new HashMap<>();
        followCounts.put("followersCount", followersCount);
        followCounts.put("followingCount", followingCount);

        return followCounts;
    }
}
