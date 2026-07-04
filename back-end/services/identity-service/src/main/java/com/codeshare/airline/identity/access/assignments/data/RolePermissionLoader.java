package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import com.codeshare.airline.identity.access.assignments.repository.RolePermissionRepository;
import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RolePermissionLoader {

    private final PermissionRepository permRepo;
    private final RoleRepository roleRepo;
    private final RolePermissionRepository repo;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("RolePermissionLoader: assigning permissions to roles for tenant {}", tenantId);
        assignTenantPermissions(tenantId);
    }

    private void assignTenantPermissions(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        List<Permission> permissions = permRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (permissions.isEmpty() || roles.isEmpty()) {
            log.warn("Skipping tenant [{}] because roles or permissions are missing", tenant.getTenantCode());
            return;
        }

        Set<String> existingMappings = repo.findMappings(tenant);
        List<RolePermission> toSave = new ArrayList<>();

        Map<String, Permission> permissionByCode = new HashMap<>();
        permissions.forEach(permission -> permissionByCode.put(permission.getCode(), permission));

        Map<String, Role> roleByCode = new HashMap<>();
        roles.forEach(role -> roleByCode.put(role.getCode(), role));

        bootstrapData.rolePermissions().forEach((roleCode, permissionCodes) -> {
            Role role = roleByCode.get(roleCode);
            if (role == null) {
                log.warn("Role [{}] not found for role-permission bootstrap", roleCode);
                return;
            }

            if (permissionCodes.contains("*")) {
                permissions.forEach(permission -> saveRolePermission(tenant, role, permission, existingMappings, toSave));
                return;
            }

            for (String permissionCode : permissionCodes) {
                Permission permission = permissionByCode.get(permissionCode);
                if (permission == null) {
                    log.warn("Permission [{}] not found for role [{}]", permissionCode, roleCode);
                    continue;
                }

                saveRolePermission(tenant, role, permission, existingMappings, toSave);
            }
        });

        if (!toSave.isEmpty()) {
            repo.saveAll(toSave);
            log.info("Tenant [{}]: {} role-permission mappings created.", tenant.getTenantCode(), toSave.size());
        } else {
            log.info("Tenant [{}]: role-permission mappings already exist.", tenant.getTenantCode());
        }
    }

    private void saveRolePermission(Tenant tenant,
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
                .tenant(tenant)
                .role(role)
                .permission(permission)
                .build());
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = repo.countByTenantId(tenantId);
        return actual >= bootstrapData.rolePermissions().size();
    }
}
