package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantOrganization;
import com.codeshare.airline.tenant.repository.TenantOrganizationRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.codeshare.airline.tenant.utils.data.TenantLoader.TENANTS;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantOrganizationLoader {

    private final TenantOrganizationRepository repo;
    private final TenantRepository tenantRepository;

    public void organizationLoad() {

        if (repo.count() > 0) {
            log.info("✔ TenantOrganization already present — skipping load.");
            return;
        }

        log.info("⏳ Loading global master Tenant Organizations...");

        List<TenantOrganization> tenantOrganizations = new ArrayList<>();

        for (String tenantCode : TENANTS) {

            Tenant tenant = tenantRepository.findByCode(tenantCode)
                    .orElse(null);

            if (tenant == null) {
                log.warn("⚠ Tenant '{}' not found — skipping organizations for this tenant.", tenantCode);
                continue; // Avoid inserting orphan organizations
            }

            log.info("→ Creating organizations for Tenant: {}", tenantCode);

            TenantOrganization org1 = TenantOrganization.builder()
                    .tenant(tenant)
                    .code("HQ")
                    .name("Headquarters")
                    .description("Main corporate office for tenant " + tenantCode)
                    .active(true)
                    .build();

            TenantOrganization org2 = TenantOrganization.builder()
                    .tenant(tenant)
                    .code("IT")
                    .name("IT Department")
                    .description("Technology and Support division of " + tenantCode)
                    .active(true)
                    .build();

            tenantOrganizations.add(org1);
            tenantOrganizations.add(org2);
        }

        repo.saveAll(tenantOrganizations);
        log.info("✔ Tenant Organizations loaded successfully. Total: {}", tenantOrganizations.size());
    }
}
