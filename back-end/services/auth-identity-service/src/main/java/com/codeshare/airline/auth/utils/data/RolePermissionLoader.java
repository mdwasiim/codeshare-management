package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Permission;
import com.codeshare.airline.auth.entities.Role;
import com.codeshare.airline.auth.entities.RolePermission;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.RolePermissionRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RolePermissionLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final RolePermissionRepository repo;
    private final TenantRepository tenantRepository;

    public void load(List<UUID> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ PermissionRoleLoader: Role-Permission mappings already exist — skipping load.");
            return;
        }

        log.info("⏳ PermissionRoleLoader: Assigning permissions to roles for {} tenants...", tenantIds.size());

        int total = 0;

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            total += assignTenantPermissions(tenantId);
        }

        log.info("✔ PermissionRoleLoader: Completed. {} role-permission mappings created.", total);
    }

    private int assignTenantPermissions(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Permission> permissions = permRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);



        if (permissions.isEmpty()) {
            log.warn("⚠ No permissions found for ssim {} — skipping mapping", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for ssim {} — skipping mapping", tenantId);
            return 0;
        }

        List<RolePermission> links = new ArrayList<>();

        for (Role role : roles) {
            for (Permission permission : permissions) {
                links.add(RolePermission.builder()
                        .tenant(tenant)
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
            log.warn("⚠ Invalid ssim ID '{}' — skipping...", id);
            return null;
        }
    }

    public boolean isLoaded() {
        return repo.count()>0;
    }
}
