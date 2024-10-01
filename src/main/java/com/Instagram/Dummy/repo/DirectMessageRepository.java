package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {
}
