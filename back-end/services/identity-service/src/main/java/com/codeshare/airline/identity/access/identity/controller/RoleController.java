package com.codeshare.airline.identity.access.identity.controller;

import com.codeshare.airline.platform.core.dto.tenant.RoleDTO;
import com.codeshare.airline.identity.access.identity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    // ---------------------------------------------------------
    // CREATE ROLE
    // ---------------------------------------------------------
    @PostMapping
    public RoleDTO create(@RequestBody RoleDTO dto) {
        return service.create(dto);
    }

    // ---------------------------------------------------------
    // UPDATE ROLE
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public RoleDTO update(
            @PathVariable UUID id,
            @RequestBody RoleDTO dto
    ) {
        return service.update(id, dto);
    }

    // ---------------------------------------------------------
    // DELETE ROLE
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    // ---------------------------------------------------------
    // GET ROLE BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public RoleDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    // ---------------------------------------------------------
    // GET ROLES BY TENANT
    // ---------------------------------------------------------
    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return service.getAllRoles();
    }
}
