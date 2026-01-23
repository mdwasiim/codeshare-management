package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.PermissionService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
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
    public ResponseEntity<CSMServiceResponse> create(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.create(dto)));
    }

    // ---------------------------------------------------------
    // UPDATE PERMISSION
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody PermissionDTO dto
    ) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.update(id, dto)));
    }

    // ---------------------------------------------------------
    // GET PERMISSION BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getById(id)));
    }

    // ---------------------------------------------------------
    // GET PERMISSIONS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<CSMServiceResponse> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getByTenant(tenantId)));
    }

    // ---------------------------------------------------------
    // DELETE PERMISSION
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }
}
