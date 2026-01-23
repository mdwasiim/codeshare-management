package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.model.entities.Role;
import com.codeshare.airline.auth.model.entities.Tenant;
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
public class RoleLoader {

    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;

    private static final List<String> BASE_ROLES = List.of(
            "ADMIN",
            "TENANT_ADMIN",
            "ORG_ADMIN",
            "MANAGER",
            "STAFF"
    );

    public void load(List<UUID> tenantIds) {

        log.info("⏳ RoleLoader: ensuring base roles for {} tenants...", tenantIds.size());

        List<Role> rolesToSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            for (String code : BASE_ROLES) {

                boolean exists =
                        roleRepository.existsByTenantAndCode(tenant, code);

                if (exists) {
                    continue;
                }

                rolesToSave.add(
                        Role.builder()
                                .tenant(tenant)
                                .code(code)
                                .name(toReadableName(code))
                                .description(code + " role for tenant " + tenant.getTenantCode())
                                .build()
                );
            }
        }

        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
            log.info("✔ RoleLoader: {} roles created.", rolesToSave.size());
        } else {
            log.info("✔ RoleLoader: all roles already exist.");
        }
    }

    public boolean isLoaded() {
        return roleRepository.count()>0;
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            log.warn("⚠ Invalid tenant UUID '{}'", id);
            return null;
        }
    }

    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}

