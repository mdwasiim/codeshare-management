package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Role;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.RoleRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

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

            // 🔥 PERFORMANCE FIX: fetch once
            Set<String> existingCodes =
                    roleRepository.findCodesByTenant(tenant);

            for (String code : BASE_ROLES) {

                if (existingCodes.contains(code)) continue;

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
            log.info("✅ RoleLoader: {} roles created.", rolesToSave.size());
        } else {
            log.info("✅ RoleLoader: all roles already exist.");
        }
    }

    // ===============================
    // 🔥 TENANT-AWARE VALIDATION
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long expected = BASE_ROLES.size();
        long actual = roleRepository.countByTenantId(tenantId);

        return actual >= expected;
    }

    // ===============================
    // HELPERS
    // ===============================
    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}