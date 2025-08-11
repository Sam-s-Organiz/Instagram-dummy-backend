package com.Instagram.Dummy.repo;

import com.Instagram.Dummy.modals.Tenant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
  Optional<Tenant> findByTenantId(String tenantId);
}
