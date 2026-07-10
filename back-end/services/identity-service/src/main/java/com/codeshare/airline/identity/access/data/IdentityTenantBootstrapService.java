package com.codeshare.airline.identity.access.data;

import com.codeshare.airline.identity.access.assignments.data.GroupMenuLoader;
import com.codeshare.airline.identity.access.assignments.data.GroupRoleLoader;
import com.codeshare.airline.identity.access.assignments.data.RolePermissionLoader;
import com.codeshare.airline.identity.access.assignments.data.UserGroupLoader;
import com.codeshare.airline.identity.access.authorization.data.MenuLoader;
import com.codeshare.airline.identity.access.authorization.data.PermissionLoader;
import com.codeshare.airline.identity.access.identity.data.GroupLoader;
import com.codeshare.airline.identity.access.identity.data.RoleLoader;
import com.codeshare.airline.identity.access.identity.data.TenantLoader;
import com.codeshare.airline.identity.access.identity.data.UserLoader;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityTenantBootstrapService {

    private final TenantLoader tenantLoader;
    private final TenantRepository tenantRepository;
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
        tenantLoader.loadTenants();
        List<UUID> tenantIds = tenantLoader.getAllTenantIds();
        for (UUID tenantId : tenantIds) {
            bootstrapTenant(tenantId);
        }
    }

    public synchronized void bootstrapTenantByCode(String tenantCode) {
        tenantLoader.loadTenants();
        Tenant tenant = tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new IllegalStateException("Tenant mirror not found after sync: " + tenantCode));
        bootstrapTenant(tenant.getId());
    }

    public boolean isInitialized() {
        return tenantLoader.getAllTenantIds().stream().allMatch(this::isTenantInitialized);
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
