package com.codeshare.airline.tenant.conbtroller;


import com.codeshare.airline.common.tenant.model.OrganizationDTO;
import com.codeshare.airline.tenant.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDTO> create(@RequestBody OrganizationDTO dto) {
        return ResponseEntity.ok(organizationService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDTO> update(@PathVariable UUID id, @RequestBody OrganizationDTO dto) {
        return ResponseEntity.ok(organizationService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(organizationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(organizationService.getAllByTenant(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
