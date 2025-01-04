package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.UserRepository;
import com.Instagram.Dummy.utils.AuthenticatedUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    @Autowired
    public AuthenticatedUserUtil authenticatedUserUtil;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void followUser(Long followingId) {
        User follower = AuthenticatedUserUtil.getAuthenticatedUser(); // Fetch authenticated user
        User following = findUserById(followingId);

        validateFollowAction(follower, following);

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }

    public void unfollowUser(Long followingId) {
        User follower = AuthenticatedUserUtil.getAuthenticatedUser(); // Fetch authenticated user
        User following = findUserById(followingId);

        validateUnfollowAction(follower, following);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Follow relationship does not exist"));

        followRepository.delete(follow);
    }

    private void validateUnfollowAction(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new RuntimeException("A user cannot unfollow themselves");
        }

        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RuntimeException("User is not following this account");
        }
    }


    public Map<String, Integer> getFollowCounts(Long userId) {
        int followersCount = followRepository.countFollowers(userId);
        int followingCount = followRepository.countFollowing(userId);

        Map<String, Integer> followCounts = new HashMap<>();
        followCounts.put("followersCount", followersCount);
        followCounts.put("followingCount", followingCount);

        return followCounts;
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
