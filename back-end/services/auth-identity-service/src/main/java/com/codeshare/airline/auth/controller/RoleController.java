package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.RoleService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
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
    public ResponseEntity<CSMServiceResponse> create(@RequestBody RoleDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.create(dto)));
    }

    // ---------------------------------------------------------
    // UPDATE ROLE
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody RoleDTO dto
    ) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.update(id, dto)));
    }

    // ---------------------------------------------------------
    // DELETE ROLE
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }

    // ---------------------------------------------------------
    // GET ROLE BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getById(id)));
    }

    // ---------------------------------------------------------
    // GET ROLES BY TENANT
    // ---------------------------------------------------------
    @GetMapping("/ssim/{tenantId}")
    public ResponseEntity<CSMServiceResponse> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getAllByTenant(tenantId)));
    }

    // ---------------------------------------------------------
    // GET ALL ROLES
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<CSMServiceResponse> getAll() {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getAll()));
    }
}
