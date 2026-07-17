package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import com.codeshare.airline.identity.access.assignments.repository.RolePermissionRepository;
import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RolePermissionLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final RolePermissionRepository repo;
    private final IdentityBootstrapData bootstrapData;
    private final HostAirlineTenantClient tenantClient;

    public void load(Long tenantId) {
        log.info("RolePermissionLoader: assigning permissions to roles for tenant {}", tenantId);
        assignTenantPermissions(tenantId);
    }

    private void assignTenantPermissions(Long tenantId) {
        List<Permission> permissions = permRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);
        String tenantCode = resolveTenantCode(tenantId);

        if (permissions.isEmpty() || roles.isEmpty()) {
            log.warn("Skipping tenant [{}] because roles or permissions are missing", tenantCode);
            return;
        }

        Set<String> existingMappings = repo.findMappingsByTenantId(tenantId);
        List<RolePermission> toSave = new ArrayList<>();

        Map<String, Permission> permissionByCode = new HashMap<>();
        permissions.forEach(permission -> permissionByCode.put(normalize(permission.getCode()), permission));

        Map<String, Role> roleByCode = new HashMap<>();
        roles.forEach(role -> roleByCode.put(normalize(role.getCode()), role));

        bootstrapData.rolePermissions().forEach((roleCode, permissionCodes) -> {
            Role role = roleByCode.get(normalize(roleCode));
            if (role == null) {
                log.warn("Role [{}] not found for role-permission bootstrap", roleCode);
                return;
            }

            if (permissionCodes.contains("*")) {
                permissions.forEach(permission -> saveRolePermission(tenantId, role, permission, existingMappings, toSave));
                return;
            }

            for (String permissionCode : permissionCodes) {
                Permission permission = permissionByCode.get(normalize(permissionCode));
                if (permission == null) {
                    log.warn("Permission [{}] not found for role [{}]", permissionCode, roleCode);
                    continue;
                }

                saveRolePermission(tenantId, role, permission, existingMappings, toSave);
            }
        });

        if (!toSave.isEmpty()) {
            repo.saveAll(toSave);
            log.info("Tenant [{}]: {} role-permission mappings created.", tenantCode, toSave.size());
        } else {
            log.info("Tenant [{}]: role-permission mappings already exist.", tenantCode);
        }
    }

    private void saveRolePermission(Long tenantId,
                                    Role role,
                                    Permission permission,
                                    Set<String> existingMappings,
                                    List<RolePermission> toSave) {
        String key = role.getCode() + ":" + permission.getCode();
        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);
        toSave.add(RolePermission.builder()
                .tenantId(tenantId)
                .role(role)
                .permission(permission)
                .build());
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    public boolean isLoaded(Long tenantId) {
        long actual = repo.countByTenantId(tenantId);
        return actual >= bootstrapData.rolePermissions().size();
    }

    private String resolveTenantCode(Long tenantId) {
        return tenantClient.getAll().stream()
                .filter(tenant -> tenantId.equals(tenant.getId()))
                .map(tenant -> tenant.getTenantCode())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tenant not found in tenant-service: " + tenantId));
    }
}
