package com.Instagram.Dummy.controllers;

import com.Instagram.Dummy.modals.Tenant;
import com.Instagram.Dummy.services.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {
  private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

  @Autowired private TenantService tenantService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> createTenant(@RequestBody Tenant tenant) {
    logger.info("Create Tenant request received: {}", tenant);
    Tenant createdTenant = tenantService.createTenant(tenant);
    return new ResponseEntity<>(createdTenant.getTenantId(), HttpStatus.CREATED);
  }
}
