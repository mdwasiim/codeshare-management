package com.codeshare.airline.tenant.management.controller;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantLoginOptionDTO;
import com.codeshare.airline.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/tenants")
    public TenantDTO create(@RequestBody TenantDTO dto) {
        return tenantService.create(dto);
    }

    @PutMapping("/tenants/{id}")
    public TenantDTO update(@PathVariable Long id, @RequestBody TenantDTO dto) {
        return tenantService.update(id, dto);
    }

    @GetMapping("/tenants/{id}")
    public TenantDTO getById(@PathVariable Long id) {
        return tenantService.getById(id);
    }

    @GetMapping("/tenants/code/{code}")
    public TenantDTO getByCode(@PathVariable String code) {
        return tenantService.getByCode(code);
    }

    @GetMapping("/tenants/code/{code}/auth-context")
    public TenantAuthContextDTO getPublicAuthContext(@PathVariable String code) {
        return tenantService.getAuthContextByCode(code);
    }

    @GetMapping("/tenants")
    public List<TenantDTO> getAll() {
        return tenantService.getAll();
    }

    @GetMapping("/tenants/login-options")
    public List<TenantLoginOptionDTO> getLoginOptions() {
        return tenantService.getLoginOptions();
    }

    @DeleteMapping("/tenants/{id}")
    public String delete(@PathVariable Long id) {
        tenantService.delete(id);
        return CSMConstants.NO_DATA;
    }

    @GetMapping("/internal/tenants/code/{code}/auth-context")
    public TenantAuthContextDTO getAuthContext(@PathVariable String code) {
        return tenantService.getAuthContextByCode(code);
    }
}
