package com.Instagram.Dummy.services;

import com.Instagram.Dummy.config.JwtUserDetails;
import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void followUser(Long followingId) {
        User follower = getAuthenticatedUser();
        User following = findUserById(followingId);

        validateFollowAction(follower, following);

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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        return jwtUserDetails.getUser();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    private void validateFollowAction(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new RuntimeException("A user cannot follow themselves");
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RuntimeException("User is already following this account");
        }
    }
}
