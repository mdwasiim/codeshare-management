package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
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

    private final OidcIdentityProviderDataLoader oidcIdentityProviderDataLoader;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info(" Tenant-service started. Initializing data...");
        init();
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 60000)
    public void retryIfNeeded() {
        if (!isInitialized()) {
            log.warn("🔁 Tenant initialization incomplete. Retrying...");
            init();
        }
    }

    private synchronized void init() {
        if (isInitialized()) {
            return;
        }

        try {
            log.info("Starting Tenant Data Initialization");

            List<Tenant> tenants = tenantLoader.loadTenants();

            if (!tenants.isEmpty()) {

                List<UUID> tenantIds = tenants.stream().map(CSMDataAbstractEntity::getId).toList();

                oidcIdentityProviderDataLoader.load(tenantIds);
                roleLoader.load(tenantIds);
                permissionLoader.load(tenantIds);
                groupLoader.load(tenantIds);
                menuLoader.load(tenantIds);

                groupRoleLoader.load(tenantIds);
                rolePermissionLoader.load(tenantIds);
                groupMenuLoader.load(tenantIds);

                userLoader.loadUser(tenants);

                userGroupDataLoader.load(tenantIds);

            } else {
                log.info(" Tenants already present — skipping load.");
            }
            log.info("🎉 Tenant Data Initialization COMPLETED");

        } catch (Exception ex) {
            log.error(" Tenant initialization failed. Will retry.", ex);
        }
    }

    /**
     * Derive initialization state from DB, NOT memory.
     */
    private boolean isInitialized() {
        return roleLoader.isLoaded()
                && permissionLoader.isLoaded()
                && groupLoader.isLoaded()
                && menuLoader.isLoaded()
                && groupRoleLoader.isLoaded()
                && rolePermissionLoader.isLoaded()
                && groupMenuLoader.isLoaded();
    }
}

