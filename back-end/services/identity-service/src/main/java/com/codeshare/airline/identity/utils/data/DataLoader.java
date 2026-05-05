package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.identity.entities.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader {

    private final TenantLoader tenantLoader;

    private final RoleLoader roleLoader;
    private final PermissionLoader permissionLoader;
    private final GroupLoader groupLoader;
    private final MenuLoader menuLoader;

    private final GroupRoleLoader groupRoleLoader;
    private final RolePermissionLoader rolePermissionLoader;
    private final GroupMenuLoader groupMenuLoader;

    private final UserLoader userLoader;
    private final UserGroupDataLoader userGroupDataLoader;

    private final OidcIdentityProviderDataLoader oidcLoader;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("🚀 Tenant-service started. Initializing data...");
        init();
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 60000)
    public void retryIfNeeded() {
        if (!isInitialized()) {
            log.warn("🔁 Initialization incomplete. Retrying...");
            init();
        }
    }

    private synchronized void init() {

        try {
            log.info("⏳ Starting Tenant Data Initialization");

            List<Tenant> tenants = tenantLoader.loadTenants();

            if (tenants.isEmpty()) {
                log.info("✔ Tenants already exist — skipping bootstrap.");
                return;
            }

            for (Tenant tenant : tenants) {

                UUID tenantId = tenant.getId();

                if (isTenantInitialized(tenantId)) {
                    log.info("✔ Tenant [{}] already initialized — skipping",
                            tenant.getTenantCode());
                    continue;
                }

                log.info("➡ Initializing tenant [{}]", tenant.getTenantCode());

                // =============================
                // CORE LOADERS
                // =============================
                oidcLoader.load(List.of(tenantId));
                roleLoader.load(List.of(tenantId));
                permissionLoader.load(List.of(tenantId));
                groupLoader.load(List.of(tenantId));
                menuLoader.load(List.of(tenantId));

                // =============================
                // MAPPINGS
                // =============================
                groupRoleLoader.load(List.of(tenantId));
                rolePermissionLoader.load(List.of(tenantId));
                groupMenuLoader.load(List.of(tenantId));

                // =============================
                // USERS
                // =============================
                userLoader.loadUser(List.of(tenant));
                userGroupDataLoader.load(List.of(tenantId));

                log.info("✅ Tenant [{}] initialized successfully",
                        tenant.getTenantCode());
            }

            log.info("🎉 Tenant Data Initialization COMPLETED");

        } catch (Exception ex) {
            log.error("❌ Initialization failed. Will retry.", ex);
        }
    }

    // ===============================
    // 🔥 GLOBAL CHECK
    // ===============================
    private boolean isInitialized() {

        List<Tenant> tenants = tenantLoader.loadTenants();

        return tenants.stream()
                .map(CSMDataAbstractEntity::getId)
                .allMatch(this::isTenantInitialized);
    }

    // ===============================
    // 🔐 TENANT CHECK
    // ===============================
    private boolean isTenantInitialized(UUID tenantId) {

        return roleLoader.isLoaded(tenantId)
                && permissionLoader.isLoaded(tenantId)
                && groupLoader.isLoaded(tenantId)
                && menuLoader.isLoaded(tenantId)
                && groupRoleLoader.isLoaded(tenantId)
                && rolePermissionLoader.isLoaded(tenantId)
                && groupMenuLoader.isLoaded(tenantId)
                && oidcLoader.isLoaded(tenantId);
    }
}
