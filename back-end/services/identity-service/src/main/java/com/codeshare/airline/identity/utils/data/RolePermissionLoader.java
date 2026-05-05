package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.*;
import com.codeshare.airline.identity.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RolePermissionLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final RolePermissionRepository repo;
    private final TenantRepository tenantRepository;

    public void load(List<UUID> tenantIds) {

        log.info("⏳ RolePermissionLoader: Assigning permissions to roles...");

        int total = 0;

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            total += assignTenantPermissions(tenantId);
        }

        log.info("✅ RolePermissionLoader: Completed. {} mappings created.", total);
    }

    private int assignTenantPermissions(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Permission> permissions = permRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (permissions.isEmpty()) {
            log.warn("⚠ No permissions found for tenant {} — skipping", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping", tenantId);
            return 0;
        }

        // 🔥 Load existing mappings once
        Set<String> existingMappings = repo.findMappings(tenant);

        List<RolePermission> toSave = new ArrayList<>();

        for (Role role : roles) {

            for (Permission permission : permissions) {

                String key = role.getCode() + ":" + permission.getCode();

                if (existingMappings.contains(key)) continue;

                toSave.add(
                        RolePermission.builder()
                                .tenant(tenant)
                                .role(role)
                                .permission(permission)
                                .build()
                );
            }
        }

        if (!toSave.isEmpty()) {
            repo.saveAll(toSave);
        }

        log.info("Tenant {}: {} role-permission mappings created.", tenant.getTenantCode(), toSave.size());

        return toSave.size();
    }

    // ===============================
    // 🔥 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long roleCount = roleRepo.countByTenantId(tenantId);
        long permCount = permRepo.countByTenantId(tenantId);
        long expected = roleCount * permCount;

        long actual = repo.countByTenantId(tenantId);

        return actual >= expected;
    }
}