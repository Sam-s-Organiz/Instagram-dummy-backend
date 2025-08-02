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

  // Method to resolve tenant from request (e.g., subdomain)
  public String resolveTenantIdFromRequest(HttpServletRequest request) {
    // Example: Extract from subdomain or header
    String host = request.getHeader("Host");
    return host.split("\\.")[0];
  }
}
