package com.codeshare.airline.identity.access.authorization.controller;

import com.codeshare.airline.identity.access.common.ExactFilter;
import com.codeshare.airline.platform.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.identity.access.authorization.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @PathVariable Long id,
            @RequestBody PermissionDTO dto
    ) {
        return service.update(id, dto);
    }

    // ---------------------------------------------------------
    // GET PERMISSION BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public PermissionDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ---------------------------------------------------------
    // GET PERMISSIONS BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public List<PermissionDTO> getAllTenant(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(service.getAllTenant(), filters);
    }

    // ---------------------------------------------------------
    // DELETE PERMISSION
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
