package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);

}
