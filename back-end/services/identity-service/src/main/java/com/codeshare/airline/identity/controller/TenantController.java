package com.codeshare.airline.identity.controller;

import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.identity.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public TenantDTO create(@RequestBody TenantDTO dto) {
        return tenantService.create(dto);
    }

    @PutMapping("/{id}")
    public TenantDTO update(
            @PathVariable UUID id,
            @RequestBody TenantDTO dto
    ) {
        return tenantService.update(id, dto);
    }

    @GetMapping("/{id}")
    public TenantDTO getById(@PathVariable UUID id) {
        return tenantService.getById(id);
    }

    @GetMapping("/code/{code}")
    public TenantDTO getByCode(@PathVariable String code) {
        return tenantService.getByCode(code);
    }

    @GetMapping
    public List<TenantDTO> getAllTenants() {
        return tenantService.getAll();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        tenantService.delete(id);
        return CSMConstants.NO_DATA;
    }
}
