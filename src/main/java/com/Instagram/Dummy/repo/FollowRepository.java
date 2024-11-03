package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    // Count followers by finding records where the given user is the following user
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    int countFollowers(Long userId);

    // Count following by finding records where the given user is the follower
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    int countFollowing(Long userId);
}
