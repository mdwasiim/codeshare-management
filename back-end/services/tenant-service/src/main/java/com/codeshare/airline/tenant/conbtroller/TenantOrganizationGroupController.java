package com.codeshare.airline.tenant.conbtroller;

import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupDTO;
import com.codeshare.airline.tenant.service.TenantOrganizationGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-groups")
@RequiredArgsConstructor
public class TenantOrganizationGroupController {

    private final TenantOrganizationGroupService service;

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody TenantOrganizationGroupDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(@PathVariable UUID id, @RequestBody TenantOrganizationGroupDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(service.getById(id)));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ServiceResponse> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getByTenant(tenantId)));
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<ServiceResponse> getByOrg(@PathVariable UUID orgId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getByOrganization(orgId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
