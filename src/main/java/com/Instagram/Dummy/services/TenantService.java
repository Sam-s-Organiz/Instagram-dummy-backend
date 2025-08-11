package com.Instagram.Dummy.services;

import com.Instagram.Dummy.modals.Tenant;
import com.Instagram.Dummy.repo.TenantRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TenantService {
  @Autowired private TenantRepository tenantRepository;

  public Tenant getTenantById(String tenantId) {
    return tenantRepository
        .findByTenantId(tenantId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));
  }

  public Tenant createTenant(Tenant tenant) {
    if (tenant.getTenantId() == null || tenant.getTenantId().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant ID is required");
    }
    if (tenantRepository.findByTenantId(tenant.getTenantId()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Tenant already exists");
    }
    return tenantRepository.save(tenant);
  }

  public String validateTenantId(String tenantId) {
    if (tenantId == null || tenantId.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant ID is required");
    }
    getTenantById(tenantId); // This will throw NOT_FOUND if invalid
    return tenantId;
  }

  // Method to resolve tenant from request (e.g., subdomain)
  public String resolveTenantIdFromRequest(HttpServletRequest request) {
    // Example: Extract from subdomain or header
    String host = request.getHeader("Host");
    return host.split("\\.")[0];
  }
}
