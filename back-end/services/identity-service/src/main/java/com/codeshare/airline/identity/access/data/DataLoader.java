package com.codeshare.airline.identity.access.data;

import com.codeshare.airline.identity.access.assignments.data.GroupMenuLoader;
import com.codeshare.airline.identity.access.assignments.data.GroupRoleLoader;
import com.codeshare.airline.identity.access.assignments.data.RolePermissionLoader;
import com.codeshare.airline.identity.access.assignments.data.UserGroupLoader;
import com.codeshare.airline.identity.access.authentication.data.OidcIdentityProviderDataLoader;
import com.codeshare.airline.identity.access.authorization.data.MenuLoader;
import com.codeshare.airline.identity.access.authorization.data.PermissionLoader;
import com.codeshare.airline.identity.access.identity.data.GroupLoader;
import com.codeshare.airline.identity.access.identity.data.RoleLoader;
import com.codeshare.airline.identity.access.identity.data.TenantLoader;
import com.codeshare.airline.identity.access.identity.data.UserLoader;
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
    private final UserGroupLoader userGroupLoader;

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

            // Ensure default tenants exist
            tenantLoader.loadTenants();

            // Fetch all tenants
            List<UUID> tenantIds = tenantLoader.getAllTenantIds();
            for (UUID tenantId : tenantIds) {

                if (isTenantInitialized(tenantId)) {
                    log.info(" Tenant [{}] already initialized — skipping", tenantId);
                    continue;
                }

                log.info(" Initializing tenant [{}]", tenantId);

                // =============================
                // CORE LOADERS
                // =============================
                oidcLoader.load(tenantId);
                roleLoader.load(tenantId);
                permissionLoader.load(tenantId);
                groupLoader.load(tenantId);
                menuLoader.load(tenantId);

                // =============================
                // MAPPINGS
                // =============================
                groupRoleLoader.load(tenantId);
                rolePermissionLoader.load(tenantId);
                groupMenuLoader.load(tenantId);

                // =============================
                // USERS
                // =============================
                userLoader.loadUser(tenantId);
                userGroupLoader.load(tenantId);

                log.info("✅ Tenant [{}] initialized successfully",
                        tenantId);
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

        List<UUID> tenantIds =
                tenantLoader.getAllTenantIds();

        return tenantIds.stream()
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
                && userLoader.isLoaded(tenantId)
                && userGroupLoader.isLoaded(tenantId)
                && oidcLoader.isLoaded(tenantId);
    }
}
