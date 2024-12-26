package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = """
                SELECT u 
                FROM User u 
                WHERE 
                    LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%')) 
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<User> findUsersByUsernameOrEmailContaining(@Param("term") String term, Pageable pageable);
}
