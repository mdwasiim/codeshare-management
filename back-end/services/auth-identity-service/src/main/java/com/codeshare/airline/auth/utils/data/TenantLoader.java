package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.TenantRepository;
import com.codeshare.airline.core.enums.common.TenantPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantLoader {

    private final TenantRepository repo;

    @Transactional
    public List<Tenant> loadTenants() {

        log.info("⏳ Bootstrapping tenants (full audit fields)...");

        Tenant tenantQR  =createTenantIfNotExists(
                "QR",
                "Qatar Airways",
                "qr@qatarairways.com.qa",
                TenantPlan.PRO,
                "GLOBAL"
        );

       /* Tenant tenantBA =  createTenantIfNotExists(
                "BA",
                "British Airways",
                "support@ba.com",
                TenantPlan.PRO,
                "EU"
        );*/

        log.info("✔ Tenant bootstrap completed");
        return List.of(tenantQR);
    }

    private Tenant createTenantIfNotExists(
            String code,
            String name,
            String email,
            TenantPlan plan,
            String region
    ) {

        if (repo.existsByTenantCode(code)) {
            log.info("Tenant '{}' already exists. Skipping.", code);
            return null;
        }

        LocalDateTime now = LocalDateTime.now();

        Tenant tenant = Tenant.builder()

                /* -------------------------
                 * Tenant domain fields
                 * ------------------------- */
                .tenantCode(code)
                .name(name)
                .description("Auto-loaded ingestion: " + code)
                .active(true)
                .plan(plan)
                .trial(false)
                .contactEmail(email)
                .contactPhone(null)
                .logoUrl(null)
                .region(region)

                /* -------------------------
                 * Subscription fields
                 * ------------------------- */
                .subscriptionStart(now)
                .subscriptionEnd(null)

                /* -------------------------
                 * Audit fields
                 * ------------------------- */
                .createdAt(now)
                .createdBy("SYSTEM")
                .updatedAt(null)
                .updatedBy(null)

                /* -------------------------
                 * State fields
                 * ------------------------- */
                .active(true)
                .isDeleted(false)
                .deletedAt(null)
                .deletedBy(null)

                /* -------------------------
                 * Trace fields
                 * ------------------------- */
                .transactionId("BOOTSTRAP-TENANT-" + code)

                .build();

        log.info("✔ Tenant '{}' created with full audit data", code);
        return  repo.save(tenant);
    }
}
