package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData.RoleSeed;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
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
public class RoleLoader {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("RoleLoader: ensuring roles for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingCodes = roleRepository.findCodesByTenant(tenant);
        List<Role> rolesToSave = new ArrayList<>();

        for (RoleSeed seed : bootstrapData.roles()) {
            if (existingCodes.contains(seed.code())) {
                continue;
            }

            existingCodes.add(seed.code());
            rolesToSave.add(Role.builder()
                    .tenant(tenant)
                    .code(seed.code())
                    .name(seed.name())
                    .description(seed.description())
                    .build());
        }

        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
            log.info("RoleLoader: {} roles created.", rolesToSave.size());
        } else {
            log.info("RoleLoader: all roles already exist.");
        }
    }

    public boolean isLoaded(UUID tenantId) {
        long expected = bootstrapData.roles().size();
        long actual = roleRepository.countByTenantId(tenantId);
        return actual >= expected;
    }
}
