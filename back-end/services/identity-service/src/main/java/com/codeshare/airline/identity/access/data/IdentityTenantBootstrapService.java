package com.codeshare.airline.identity.access.data;

import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.identity.access.assignments.data.GroupMenuLoader;
import com.codeshare.airline.identity.access.assignments.data.GroupRoleLoader;
import com.codeshare.airline.identity.access.assignments.data.RolePermissionLoader;
import com.codeshare.airline.identity.access.assignments.data.UserGroupLoader;
import com.codeshare.airline.identity.access.authorization.data.MenuLoader;
import com.codeshare.airline.identity.access.authorization.data.PermissionLoader;
import com.codeshare.airline.identity.access.identity.data.GroupLoader;
import com.codeshare.airline.identity.access.identity.data.RoleLoader;
import com.codeshare.airline.identity.access.identity.data.UserLoader;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityTenantBootstrapService {

    private final HostAirlineTenantClient tenantClient;
    private final RoleLoader roleLoader;
    private final PermissionLoader permissionLoader;
    private final GroupLoader groupLoader;
    private final MenuLoader menuLoader;
    private final GroupRoleLoader groupRoleLoader;
    private final RolePermissionLoader rolePermissionLoader;
    private final GroupMenuLoader groupMenuLoader;
    private final UserLoader userLoader;
    private final UserGroupLoader userGroupLoader;

    public synchronized void bootstrapAllTenants() {
        List<UUID> tenantIds = tenantClient.getAll().stream()
                .map(TenantDTO::getId)
                .toList();
        for (UUID tenantId : tenantIds) {
            bootstrapTenant(tenantId);
        }
    }

    public synchronized void bootstrapTenantByCode(String tenantCode) {
        UUID tenantId = tenantClient.getAll().stream()
                .filter(tenant -> tenantCode.equalsIgnoreCase(tenant.getTenantCode()))
                .map(TenantDTO::getId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tenant not found in tenant-service: " + tenantCode));
        bootstrapTenant(tenantId);
    }

    public boolean isInitialized() {
        return tenantClient.getAll().stream()
                .map(TenantDTO::getId)
                .allMatch(this::isTenantInitialized);
    }

    private void bootstrapTenant(UUID tenantId) {
        log.info("Ensuring bootstrap data for tenant [{}]", tenantId);

        roleLoader.load(tenantId);
        permissionLoader.load(tenantId);
        groupLoader.load(tenantId);
        menuLoader.load(tenantId);
        groupRoleLoader.load(tenantId);
        rolePermissionLoader.load(tenantId);
        groupMenuLoader.load(tenantId);
        userLoader.loadUser(tenantId);
        userGroupLoader.load(tenantId);

        log.info("Tenant [{}] bootstrap data ensured", tenantId);
    }

    private boolean isTenantInitialized(UUID tenantId) {
        return roleLoader.isLoaded(tenantId)
                && permissionLoader.isLoaded(tenantId)
                && groupLoader.isLoaded(tenantId)
                && menuLoader.isLoaded(tenantId)
                && groupRoleLoader.isLoaded(tenantId)
                && rolePermissionLoader.isLoaded(tenantId)
                && groupMenuLoader.isLoaded(tenantId)
                && userLoader.isLoaded(tenantId)
                && userGroupLoader.isLoaded(tenantId);
    }
}
