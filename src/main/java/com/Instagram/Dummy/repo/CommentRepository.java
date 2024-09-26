package com.Instagram.Dummy.repo;

 import com.Instagram.Dummy.modals.Comment;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
