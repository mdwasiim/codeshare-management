package com.codeshare.airline.tenant.utils.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantDataLoader implements ApplicationRunner {

    private final TenantLoader tenantLoader;
    private final TenantDataSourceLoader dataSourceLoader;
    private final TenantOrganizationLoader tenantOrganizationLoader;

    @Override
    public void run(ApplicationArguments args) {

        log.info("⏳ Starting Tenant Data Initialization...");

        try {
            log.info("→ Loading master datasource list...");
            dataSourceLoader.loadTenantDataSource();
            log.info("✔ Master datasources loaded.");
        } catch (Exception ex) {
            log.error("❌ Error loading datasources: {}", ex.getMessage(), ex);
        }

        try {
            log.info("→ Loading tenants...");
            tenantLoader.tenantLoader();
            log.info("✔ Tenants loaded.");
        } catch (Exception ex) {
            log.error("❌ Error loading tenants: {}", ex.getMessage(), ex);
        }

        try {
            log.info("→ Loading organization metadata...");
            tenantOrganizationLoader.organizationLoad();
            log.info("✔ Organizations loaded.");
        } catch (Exception ex) {
            log.error("❌ Error loading organizations: {}", ex.getMessage(), ex);
        }

        log.info("🎉 Tenant Data Initialization Completed.");
    }
}
