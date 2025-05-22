package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Follow;
import com.Instagram.Dummy.modals.Post;
import com.Instagram.Dummy.modals.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  boolean existsBySourceUserAndTargetUser(User sourceUser, User targetUser);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.targetUser.id = :userId")
  int countFollowers(Long userId);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.sourceUser.id = :userId")
  int countFollowing(Long userId);

  @Query(
      """
    SELECT p FROM Post p
    WHERE p.user.id IN (
        SELECT f.targetUser.id FROM Follow f WHERE f.sourceUser.id = :currentUserId
    )
    ORDER BY p.createdAt DESC
    """)
  List<Post> getFeedForUser(Long currentUserId);

  @Query(
      "SELECT f.targetUser.id FROM Follow f WHERE f.sourceUser.id = :currentUserId AND f.targetUser.id IN :userIds")
  List<Long> findFollowedUserIds(Long currentUserId, List<Long> userIds);

  @Query("SELECT f.targetUser.id FROM Follow f WHERE f.sourceUser.id = :followerId")
  List<Long> findFollowingIdsByFollowerId(Long followerId);

  Optional<Follow> findBySourceUserAndTargetUser(User sourceUser, User targetUser);
}
