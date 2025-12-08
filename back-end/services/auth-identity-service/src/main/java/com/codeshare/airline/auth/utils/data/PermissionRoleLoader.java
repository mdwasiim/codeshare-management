package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.rbac.Permission;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.entities.rbac.RolePermission;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.PermissionRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionRoleLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final PermissionRoleRepository repo;

    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ PermissionRoleLoader: Role-Permission mappings already exist — skipping load.");
            return;
        }

        log.info("⏳ PermissionRoleLoader: Assigning permissions to roles for {} tenants...", tenantIds.size());

        int total = 0;

        for (String tenantIdStr : tenantIds) {

            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId == null) continue;

            total += assignTenantPermissions(tenantId);
        }

        log.info("✔ PermissionRoleLoader: Completed. {} role-permission mappings created.", total);
    }

    private int assignTenantPermissions(UUID tenantId) {

        List<Permission> permissions = permRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (permissions.isEmpty()) {
            log.warn("⚠ No permissions found for tenant {} — skipping mapping", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping mapping", tenantId);
            return 0;
        }

        List<RolePermission> links = new ArrayList<>();

        for (Role role : roles) {
            for (Permission permission : permissions) {
                links.add(RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build());
            }
        }

        repo.saveAll(links);

        log.info("✔ Tenant {}: {} permission-role mappings created.", tenantId, links.size());

        return links.size();
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid tenant ID '{}' — skipping...", id);
            return null;
        }
    }
}
