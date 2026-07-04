package com.codeshare.airline.identity.access.authorization.data;

import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.PermissionSeed;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionLoader {

    private final PermissionRepository permissionRepository;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("PermissionLoader: ensuring permissions for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingKeys = permissionRepository.findPermissionKeysByTenantId(tenantId);
        List<Permission> toSave = new ArrayList<>();

        for (PermissionSeed seed : bootstrapData.permissions()) {
            String key = seed.domain() + ":" + seed.action();
            if (existingKeys.contains(key)) {
                continue;
            }

            existingKeys.add(key);
            toSave.add(Permission.builder()
                    .tenant(tenant)
                    .name(seed.name())
                    .code(seed.code())
                    .domain(seed.domain())
                    .action(seed.action())
                    .description(seed.description())
                    .build());
        }

        if (!toSave.isEmpty()) {
            permissionRepository.saveAll(toSave);
            log.info("PermissionLoader: {} permissions created.", toSave.size());
        } else {
            log.info("PermissionLoader: all permissions already exist.");
        }
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = permissionRepository.countByTenantId(tenantId);
        long expected = bootstrapData.permissions().size();
        return actual >= expected;
    }
}
