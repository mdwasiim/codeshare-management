package com.codeshare.airline.tenant.conbtroller.internal;

import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/tenants")
@RequiredArgsConstructor
public class TenantInternalController {

    private final TenantService tenantService;

    @GetMapping
    public List<TenantDTO> getAllTenantsRaw() {
        return tenantService.getAll();
    }
}
