package com.codeshare.airline.tenant.integration.identity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "identity-service",
        url = "${services.identity.url:http://localhost:8081}"
)
public interface IdentityBootstrapClient {

    @PostMapping("/internal/bootstrap/tenants/code/{tenantCode}")
    void bootstrapTenant(@RequestHeader("X-Tenant-Id") String tenantHeader, @PathVariable("tenantCode") String tenantCode);
}
