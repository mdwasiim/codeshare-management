package com.codeshare.airline.identity.access.data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/bootstrap")
@RequiredArgsConstructor
public class InternalIdentityBootstrapController {

    private final IdentityTenantBootstrapService bootstrapService;

    @PostMapping("/tenants/code/{tenantCode}")
    public void bootstrapTenant(@PathVariable String tenantCode) {
        bootstrapService.bootstrapTenantByCode(tenantCode);
    }
}
