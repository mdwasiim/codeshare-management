package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.PermissionService;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    @PostMapping
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> update(@PathVariable UUID id, @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(service.getByTenant(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

