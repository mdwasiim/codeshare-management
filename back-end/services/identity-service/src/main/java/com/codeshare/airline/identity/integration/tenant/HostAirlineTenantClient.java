package com.codeshare.airline.identity.integration.tenant;

import com.codeshare.airline.platform.core.dto.tenant.TenantAuthContextDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "tenant-service",
        url = "${services.tenant.url:${services.host-airline.url:http://localhost:8086}}"
)
public interface HostAirlineTenantClient {

    @GetMapping("/tenants")
    List<TenantDTO> getAll();

    @GetMapping("/internal/tenants/code/{code}/auth-context")
    TenantAuthContextDTO getAuthContext(@PathVariable("code") String code);
}
