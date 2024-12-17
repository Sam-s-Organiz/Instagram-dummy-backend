package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findByUserIdIn(List<Long> userIds); // Fetch posts for multiple users


}