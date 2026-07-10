package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.enums.common.TenantPlan;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.TenantSeed;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
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
    private final HostAirlineTenantClient tenantClient;

    @Transactional
    public List<Tenant> loadTenants() {
        List<Tenant> tenants = loadFromTenantService();
        if (!tenants.isEmpty()) {
            log.info("Tenant mirror sync completed from tenant-service");
            return tenants;
        }

        log.warn("tenant-service returned no tenants. Falling back to local bootstrap tenant seeds");
        tenants = bootstrapData.tenants().stream()
                .map(this::createOrGetTenantFromSeed)
                .toList();

        log.info("Tenant bootstrap completed");
        return tenants;
    }

    private List<Tenant> loadFromTenantService() {
        try {
            return tenantClient.getAll().stream()
                    .map(this::syncTenantMirror)
                    .toList();
        } catch (Exception ex) {
            log.warn("Unable to sync tenants from tenant-service. Falling back to local bootstrap.", ex);
            return List.of();
        }
    }

    private Tenant syncTenantMirror(TenantDTO dto) {
        Optional<Tenant> existing = repo.findByTenantCode(dto.getCode());
        Tenant tenant = existing.orElseGet(() -> Tenant.builder().build());

        if (dto.getId() != null && tenant.getId() == null) {
            tenant.setId(dto.getId());
        }

        tenant.setTenantCode(dto.getCode());
        tenant.setName(dto.getName());
        tenant.setDescription(dto.getDescription());
        tenant.setPlan(dto.getPlan() == null ? null : TenantPlan.valueOf(dto.getPlan()));
        tenant.setTrial(Boolean.TRUE.equals(dto.getTrial()));
        tenant.setContactEmail(dto.getContactEmail());
        tenant.setContactPhone(dto.getContactPhone());
        tenant.setLogoUrl(dto.getLogoUrl());
        tenant.setRegion(dto.getRegion());
        tenant.setStatus(dto.getStatus());
        tenant.setSubscriptionStart(dto.getSubscriptionStart());
        tenant.setSubscriptionEnd(dto.getSubscriptionEnd());
        tenant.setActive(true);
        tenant.setDeleted(false);
        tenant.setCreatedBy(tenant.getCreatedBy() == null ? "TENANT-SYNC" : tenant.getCreatedBy());
        tenant.setCreatedAt(tenant.getCreatedAt() == null ? Instant.now() : tenant.getCreatedAt());
        tenant.setUpdatedBy("TENANT-SYNC");
        tenant.setUpdatedAt(Instant.now());
        tenant.setTransactionId("TENANT-SYNC-" + dto.getCode());

        Tenant saved = repo.save(tenant);
        log.info("Tenant mirror synced for tenant '{}'", dto.getCode());
        return saved;
    }

    private Tenant createOrGetTenantFromSeed(TenantSeed seed) {
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
