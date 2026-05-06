package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.codeshare.airline.core.enums.common.TenantPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantLoader {

    private final TenantRepository repo;

    @Transactional
    public List<Tenant> loadTenants() {

        log.info("⏳ Bootstrapping tenants...");

        Tenant tenantQR = createOrGetTenant(
                "QR",
                "Qatar Airways",
                "qr@qatarairways.com.qa",
                TenantPlan.PRO,
                "GLOBAL"
        );

       /* Tenant tenantBA = createOrGetTenant(
                "BA",
                "British Airways",
                "support@ba.com",
                TenantPlan.PRO,
                "EU"
        );*/

        log.info("✅ Tenant bootstrap completed");

        return List.of(tenantQR);
    }

    private Tenant createOrGetTenant(
            String code,
            String name,
            String email,
            TenantPlan plan,
            String region
    ) {

        Optional<Tenant> existing = repo.findByTenantCode(code);

        if (existing.isPresent()) {
            log.info("Tenant '{}' already exists. Using existing.", code);
            return existing.get(); // ✅ IMPORTANT: return existing, not null
        }

        LocalDateTime now = LocalDateTime.now();

        Tenant tenant = Tenant.builder()

                // =========================
                // BUSINESS FIELDS
                // =========================
                .tenantCode(code)
                .name(name)
                .description("Auto-loaded tenant: " + code)
                .plan(plan)
                .trial(false)
                .contactEmail(email)
                .region(region)

                // =========================
                // STATUS
                // =========================
                .active(true)
                .deleted(false)

                // =========================
                // SUBSCRIPTION
                // =========================
                .subscriptionStart(now)
                .subscriptionEnd(null)

                // =========================
                // AUDIT (only if not auto-handled)
                // =========================
                .createdAt(Instant.now())
                .createdBy("SYSTEM")

                // =========================
                // TRACE
                // =========================
                .transactionId("BOOTSTRAP-TENANT-" + code)

                .build();

        Tenant saved = repo.save(tenant);

        log.info("✅ Tenant '{}' created", code);

        return saved;
    }

    // ===============================
    // 🔥 REQUIRED FOR DATALOADER
    // ===============================
    public List<UUID> getAllTenantIds() {
        return repo.findAll()
                .stream()
                .map(Tenant::getId)
                .toList();
    }

    public boolean isLoaded(UUID tenantId) {
        return repo.existsById(tenantId);
    }
}