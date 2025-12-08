package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.common.services.utils.helper.UuidUtil;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantDataSource;
import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import com.codeshare.airline.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantLoader {

    private final TenantRepository repo;
    private final TenantDataSourceRepository dsRepo;

    public static final List<String> TENANTS = List.of(
            "CODESHARE_MANAGEMENT",
            "QATAR_AIRLINES"
    );

    public void tenantLoader() {

        if (repo.count() > 0) {
            log.info("✔ Tenants already present — skipping load.");
            return;
        }

        log.info("⏳ Loading global master tenants...");

        TenantDataSource primaryDs = dsRepo.findById(
                UuidUtil.fixed("DS-MYSQL_PRIMARY")
        ).orElseThrow(() ->
                new RuntimeException("Primary datasource DS-MYSQL_PRIMARY not found")
        );

        List<Tenant> tenants = new ArrayList<>();

        for (String code : TENANTS) {

            String email = code.toLowerCase().replace("_", ".") + "@example.com";

            Tenant tenant = Tenant.builder()
                    .code(code)
                    .name(toReadableName(code))
                    .description("Auto-loaded tenant: " + code)
                    .plan("PRO")
                    .trial(false)
                    .enabled(true)
                    .tenantDataSource(primaryDs)
                    .contactEmail(email)
                    .region("GLOBAL")
                    .createdBy("SYSTEM")
                    .build();

            tenants.add(tenant);
        }

        repo.saveAll(tenants);

        log.info("✔ {} Tenants loaded successfully.", tenants.size());
    }

    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}
