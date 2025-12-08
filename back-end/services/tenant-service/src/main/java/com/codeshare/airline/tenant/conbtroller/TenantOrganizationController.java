package com.codeshare.airline.tenant.controller;

import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import com.codeshare.airline.tenant.service.TenantOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-organizations")
@RequiredArgsConstructor
public class TenantOrganizationController {

    private final TenantOrganizationService organizationService;

    // ---------------------------------------------------------
    // CREATE ORGANIZATION
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody TenantOrganizationDTO dto) {
        return ResponseEntity.ok(
                ServiceResponse.success(organizationService.create(dto))
        );
    }

    // ---------------------------------------------------------
    // UPDATE ORGANIZATION
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody TenantOrganizationDTO dto
    ) {
        return ResponseEntity.ok(
                ServiceResponse.success(organizationService.update(id, dto))
        );
    }

    // ---------------------------------------------------------
    // GET ORGANIZATION BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ServiceResponse.success(organizationService.getById(id))
        );
    }

    // ---------------------------------------------------------
    // GET ORGANIZATIONS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(
                ServiceResponse.success(organizationService.getAllByTenant(tenantId))
        );
    }

    // ---------------------------------------------------------
    // DELETE ORGANIZATION
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        organizationService.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
