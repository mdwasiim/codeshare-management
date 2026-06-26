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

    private static final Map<String, List<String>> ROLE_PERMISSION_MAP = Map.ofEntries(

            // =========================
            // SUPER ADMIN
            // =========================
            Map.entry("SUPER_ADMIN", List.of("*")),

            // =========================
            // TENANT ADMIN
            // =========================
            Map.entry("TENANT_ADMIN", List.of(
                    "user:create",
                    "user:read",
                    "user:update",
                    "group:create",
                    "group:read",
                    "group:update",
                    "role:read",
                    "tenant:read",
                    "dashboard:read",
                    "settings:read"
            )),

            // =========================
            // IAM ADMIN
            // =========================
            Map.entry("IAM_ADMIN", List.of(
                    "user:create",
                    "user:read",
                    "user:update",
                    "user:unlock",

                    "group:create",
                    "group:read",
                    "group:update",
                    "group:assign",

                    "role:create",
                    "role:read",
                    "role:update",
                    "role:assign",

                    "tenant:read",

                    "permission:read",
                    "permission:assign"
            )),

            // =========================
            // OPS MANAGER
            // =========================
            Map.entry("OPS_MANAGER", List.of(
                    "flight:create",
                    "flight:read",
                    "flight:update",
                    "booking:read",
                    "report:read",
                    "dashboard:read"
            )),

            // =========================
            // FLIGHT OPERATOR
            // =========================
            Map.entry("FLIGHT_OPERATOR", List.of(
                    "flight:read",
                    "flight:update"
            )),

            // =========================
            // BOOKING AGENT
            // =========================
            Map.entry("BOOKING_AGENT", List.of(
                    "booking:create",
                    "booking:read",
                    "booking:update",
                    "booking:cancel"
            )),

            // =========================
            // CUSTOMER SUPPORT
            // =========================
            Map.entry("CUSTOMER_SUPPORT", List.of(
                    "booking:read",
                    "booking:update",
                    "user:read"
            )),

            // =========================
            // REPORT ANALYST
            // =========================
            Map.entry("REPORT_ANALYST", List.of(
                    "report:read",
                    "report:export",
                    "dashboard:read"
            )),

            // =========================
            // AUDITOR
            // =========================
            Map.entry("AUDITOR", List.of(
                    "audit:read",
                    "report:read"
            )),

            // =========================
            // DEFAULT USER
            // =========================
            Map.entry("USER", List.of(
                    "dashboard:read"
            ))
    );


    public void load(UUID tenantId) {

        log.info("⏳ RolePermissionLoader: Assigning permissions to roles...");

        assignTenantPermissions(tenantId);

        log.info(" RolePermissionLoader: Completed. mappings created.");
    }

    private void assignTenantPermissions(UUID tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Permission> permissions =
                permRepo.findByTenantId(tenantId);

        List<Role> roles =
                roleRepo.findByTenantId(tenantId);

        if (permissions.isEmpty()) {
            log.warn("⚠ No permissions found for tenant {} — skipping", tenantId);
            return;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping", tenantId);
            return;
        }

        // Existing mappings
        Set<String> existingMappings =
                repo.findMappings(tenant);

        List<RolePermission> toSave =
                new ArrayList<>();

        // Permission lookup
        Map<String, Permission> permissionByCode =
                new HashMap<>();

        for (Permission permission : permissions) {
            permissionByCode.put(
                    permission.getCode(),
                    permission
            );
        }

        for (Role role : roles) {

            List<String> allowedPermissions =
                    ROLE_PERMISSION_MAP.getOrDefault(
                            role.getCode(),
                            List.of()
                    );

            // SUPER_ADMIN => ALL PERMISSIONS
            if (allowedPermissions.contains("*")) {

                for (Permission permission : permissions) {

                    saveRolePermission(
                            tenant,
                            role,
                            permission,
                            existingMappings,
                            toSave
                    );
                }

                continue;
            }

            for (String permissionCode : allowedPermissions) {

                Permission permission =
                        permissionByCode.get(permissionCode);

                if (permission == null) {

                    log.warn(
                            "⚠ Permission [{}] not found for role [{}]",
                            permissionCode,
                            role.getCode()
                    );

                    continue;
                }

                saveRolePermission(
                        tenant,
                        role,
                        permission,
                        existingMappings,
                        toSave
                );
            }
        }

        if (!toSave.isEmpty()) {

            repo.saveAll(toSave);

            log.info(
                    "✅ Tenant [{}]: {} role-permission mappings created.",
                    tenant.getTenantCode(),
                    toSave.size()
            );

        } else {

            log.info(
                    "✅ Tenant [{}]: role-permission mappings already exist.",
                    tenant.getTenantCode()
            );
        }
    }

    private void saveRolePermission(
            Tenant tenant,
            Role role,
            Permission permission,
            Set<String> existingMappings,
            List<RolePermission> toSave
    ) {

        String key =
                role.getCode() + ":" + permission.getCode();

        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);

        toSave.add(
                RolePermission.builder()
                        .tenant(tenant)
                        .role(role)
                        .permission(permission)
                        .build()
        );
    }

    // ===============================
    // 🔥 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long actual = repo.countByTenantId(tenantId);

        return actual >= 0;
    }
}
