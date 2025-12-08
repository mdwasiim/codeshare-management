package com.codeshare.airline.tenant.conbtroller.internal;

import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import com.codeshare.airline.tenant.service.TenantOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/internal/tenant")
@RequiredArgsConstructor
public class TenantOrganizationInternalController {

    private final TenantOrganizationService organizationService;

    /**
     * INTERNAL USE ONLY
     * Returns list of organizations for a tenant.
     * - Used by AUTH-SERVICE during RBAC bootstrap
     * - Used by USER-SERVICE to map users to tenants
     */
    @GetMapping("/{tenantId}/organizations")
    public ServiceResponse<List<TenantOrganizationDTO>> getTenantOrganizations(
            @PathVariable UUID tenantId
    ) {
        return ServiceResponse.success(organizationService.getAllByTenant(tenantId));
    }
}
