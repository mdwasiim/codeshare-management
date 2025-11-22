package com.codeshare.airline.auth.controller.identity;


import com.codeshare.airline.auth.service.RoleService;
import com.codeshare.airline.common.auth.model.RoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(
            @PathVariable UUID id,
            @RequestBody RoleDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<RoleDTO>> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(service.getAllByTenant(tenantId));
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
