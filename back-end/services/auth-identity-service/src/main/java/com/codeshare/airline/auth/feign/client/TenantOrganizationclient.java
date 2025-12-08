package com.codeshare.airline.auth.feign.client;

import com.codeshare.airline.common.tenant.model.TenantOrganizationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "tenant-service", path = "/api/internal/tenant")
public interface TenantOrganizationclient {

    @GetMapping("/{tenantId}/organizations")
    List<TenantOrganizationDTO> getOrganizations(@PathVariable UUID tenantId);
}

