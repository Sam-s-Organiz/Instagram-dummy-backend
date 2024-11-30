package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(User follower, User following);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    int countFollowers(Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    int countFollowing(Long userId);

    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(Long followerId);
}
