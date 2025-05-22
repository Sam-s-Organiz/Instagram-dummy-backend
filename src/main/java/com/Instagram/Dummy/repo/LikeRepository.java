package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Like;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
  Set<Like> findByPostId(Long postId);

  // Check if a user has already liked a specific post
  boolean existsByUserIdAndPostId(Long userId, Long postId);

  int countByPostId(Long postId);

  Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
