package com.codeshare.airline.identity.integration.tenant;

import com.codeshare.airline.core.dto.tenant.TenantAuthContextDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "host-airline-service",
        url = "${services.host-airline.url:http://localhost:8086}"
)
public interface HostAirlineTenantClient {

    @GetMapping("/internal/tenants/code/{code}/auth-context")
    TenantAuthContextDTO getAuthContext(@PathVariable("code") String code);
}
