package com.codeshare.airline.auth.controller.rbac;

import com.codeshare.airline.auth.service.RoleService;
import com.codeshare.airline.common.auth.identity.model.RoleDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    // ---------------------------------------------------------
    // CREATE ROLE
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody RoleDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.create(dto)));
    }

    // ---------------------------------------------------------
    // UPDATE ROLE
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody RoleDTO dto
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.update(id, dto)));
    }

    // ---------------------------------------------------------
    // DELETE ROLE
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }

    // ---------------------------------------------------------
    // GET ROLE BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(service.getById(id)));
    }

    // ---------------------------------------------------------
    // GET ROLES BY TENANT
    // ---------------------------------------------------------
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ServiceResponse> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(ServiceResponse.success(service.getAllByTenant(tenantId)));
    }

    // ---------------------------------------------------------
    // GET ALL ROLES
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse> getAll() {
        return ResponseEntity.ok(ServiceResponse.success(service.getAll()));
    }
}
