package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Set<Like> findByPostId(Long postId);

    // Check if a user has already liked a specific post
    boolean existsByUserIdAndPostId(Long userId, Long postId);
}
