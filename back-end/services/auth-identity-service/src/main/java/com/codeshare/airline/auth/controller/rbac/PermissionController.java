package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.PermissionService;
import com.codeshare.airline.common.auth.identity.model.PermissionDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    // ---------------------------------------------------------
    // CREATE PERMISSION
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.create(dto)));
    }

    // ---------------------------------------------------------
    // UPDATE PERMISSION
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody PermissionDTO dto
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.update(id, dto)));
    }

    // ---------------------------------------------------------
    // GET PERMISSION BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(service.getById(id)));
    }

    // ---------------------------------------------------------
    // GET PERMISSIONS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getByTenant(tenantId)));
    }

    // ---------------------------------------------------------
    // DELETE PERMISSION
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
