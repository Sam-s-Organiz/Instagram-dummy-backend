package com.Instagram.Dummy.services;

import com.Instagram.Dummy.exceptions.BadRequestException;
import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import com.Instagram.Dummy.repo.FollowRepository;
import com.Instagram.Dummy.repo.PostRepository;
import com.Instagram.Dummy.repo.UserRepository;
import com.Instagram.Dummy.utils.AuthenticatedUserUtil;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
public class FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;
  private final AuthenticatedUserUtil authUtil;

    public FollowService(
      FollowRepository followRepository,
      UserRepository userRepository,
      AuthenticatedUserUtil authUtil,
      PostRepository postRepository) {
    this.followRepository = followRepository;
    this.userRepository = userRepository;
    this.authUtil = authUtil;
    }

  @Transactional
  public void followUser(Long targetUserId) {
    User sourceUser = authUtil.getAuthenticatedUser();
    User targetUser = getUserOrThrow(targetUserId);

    if (sourceUser.equals(targetUser)) {
      throw new BadRequestException("You cannot follow yourself.");
    }

    if (followRepository.existsBySourceUserAndTargetUser(sourceUser, targetUser)) {
      throw new BadRequestException("You are already following this user.");
    }

    Follow follow = new Follow();
    follow.setSourceUser(sourceUser);
    follow.setTargetUser(targetUser);
    followRepository.save(follow);
  }

  @Transactional
  public void unfollowUser(Long targetUserId) {
    User sourceUser = authUtil.getAuthenticatedUser();
    User targetUser = getUserOrThrow(targetUserId);

    if (sourceUser.equals(targetUser)) {
      throw new BadRequestException("You cannot unfollow yourself.");
    }

    Follow follow =
        followRepository
            .findBySourceUserAndTargetUser(sourceUser, targetUser)
            .orElseThrow(() -> new BadRequestException("You are not following this user."));

    followRepository.delete(follow);
  }

  public Map<String, Integer> getFollowCounts(Long userId) {
    return Map.of(
        "followersCount", followRepository.countFollowers(userId),
        "followingCount", followRepository.countFollowing(userId));
  }

  private User getUserOrThrow(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new BadRequestException("User not found with ID: " + userId));
  }

  public List<Long> getFollowedUserIdsIncludingSelf(Long userId) {
    List<Long> followedIds = followRepository.findFollowingIdsByFollowerId(userId);
    log.info("User {} is following (including self): {}", userId, followedIds);
    followedIds.add(userId); // Include self for own posts
    return followedIds;
  }
}
