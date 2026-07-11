package com.codeshare.airline.identity.access.authorization.data;

import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.identity.access.authorization.repository.PermissionRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.PermissionSeed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionLoader {

    private final PermissionRepository permissionRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("PermissionLoader: ensuring permissions for tenant {}", tenantId);

        Set<String> existingKeys = permissionRepository.findPermissionKeysByTenantId(tenantId).stream()
                .map(PermissionLoader::normalize)
                .collect(Collectors.toSet());
        List<Permission> toSave = new ArrayList<>();

        for (PermissionSeed seed : bootstrapData.permissions()) {
            String key = normalize(seed.domain() + ":" + seed.action());
            if (existingKeys.contains(key)) {
                continue;
            }

            existingKeys.add(key);
            toSave.add(Permission.builder()
                    .tenantId(tenantId)
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

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }
}
