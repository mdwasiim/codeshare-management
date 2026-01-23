package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.service.TenantService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<CSMServiceResponse> create(@RequestBody TenantDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(tenantService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody TenantDTO dto
    ) {
        return ResponseEntity.ok(CSMServiceResponse.success(tenantService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CSMServiceResponse.success(tenantService.getById(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CSMServiceResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(CSMServiceResponse.success(tenantService.getByCode(code)));
    }

    @GetMapping
    public ResponseEntity<CSMServiceResponse> getAllTenants() {
        return ResponseEntity.ok(CSMServiceResponse.success(tenantService.getAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> delete(@PathVariable UUID id) {
        tenantService.delete(id);
        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }
}
