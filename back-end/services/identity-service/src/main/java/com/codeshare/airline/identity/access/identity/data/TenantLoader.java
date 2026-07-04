package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.TenantSeed;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
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
    private final IdentityBootstrapData bootstrapData;

    @Transactional
    public List<Tenant> loadTenants() {
        log.info("Bootstrapping tenants from JSON resources");

        List<Tenant> tenants = bootstrapData.tenants().stream()
                .map(this::createOrGetTenant)
                .toList();

        log.info("Tenant bootstrap completed");
        return tenants;
    }

    private Tenant createOrGetTenant(TenantSeed seed) {
        Optional<Tenant> existing = repo.findByTenantCode(seed.code());
        if (existing.isPresent()) {
            log.info("Tenant '{}' already exists. Using existing.", seed.code());
            return existing.get();
        }

        LocalDateTime now = LocalDateTime.now();
        Tenant tenant = Tenant.builder()
                .tenantCode(seed.code())
                .name(seed.name())
                .description(seed.description())
                .plan(seed.plan())
                .trial(seed.trial())
                .contactEmail(seed.contactEmail())
                .contactPhone(seed.contactPhone())
                .logoUrl(seed.logoUrl())
                .region(seed.region())
                .active(true)
                .deleted(false)
                .subscriptionStart(now)
                .subscriptionEnd(null)
                .createdAt(Instant.now())
                .createdBy("SYSTEM")
                .transactionId("BOOTSTRAP-TENANT-" + seed.code())
                .build();

        Tenant saved = repo.save(tenant);
        log.info("Tenant '{}' created", seed.code());
        return saved;
    }

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
