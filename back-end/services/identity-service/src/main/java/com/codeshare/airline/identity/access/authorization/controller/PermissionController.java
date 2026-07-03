package com.codeshare.airline.identity.access.authorization.controller;

import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.identity.access.authorization.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService service;

    // ---------------------------------------------------------
    // CREATE PERMISSION
    // ---------------------------------------------------------
    @PostMapping
    public PermissionDTO create(@RequestBody PermissionDTO dto) {
        return service.create(dto);
    }

    // ---------------------------------------------------------
    // UPDATE PERMISSION
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public PermissionDTO update(
            @PathVariable UUID id,
            @RequestBody PermissionDTO dto
    ) {
        return service.update(id, dto);
    }

    // ---------------------------------------------------------
    // GET PERMISSION BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public PermissionDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    // ---------------------------------------------------------
    // GET PERMISSIONS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public List<PermissionDTO> getAllTenant() {
        return service.getAllTenant();
    }

    // ---------------------------------------------------------
    // DELETE PERMISSION
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
