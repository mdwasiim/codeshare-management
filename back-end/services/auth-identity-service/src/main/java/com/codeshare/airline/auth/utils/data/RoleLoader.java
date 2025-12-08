package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.repository.RoleRepository;
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

    private final RoleRepository repo;

    private static final List<String> BASE_ROLES = List.of(
            "ADMIN",
            "TENANT_ADMIN",
            "ORG_ADMIN",
            "MANAGER",
            "STAFF"
    );

    /**
     * Load roles for all tenants (called by DataLoader)
     */
    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ RoleLoader: roles already present — skipping load.");
            return;
        }

        log.info("⏳ RoleLoader: inserting base roles for {} tenants...", tenantIds.size());

        List<Role> roles = new ArrayList<>();

        for (String tenantId : tenantIds) {

            UUID tid;
            try {
                tid = UUID.fromString(tenantId);
            } catch (Exception ex) {
                log.warn("⚠ Invalid tenant UUID '{}', skipping...", tenantId);
                continue;
            }

            for (String baseRole : BASE_ROLES) {
                Role role = Role.builder()
                        .tenantId(tid)
                        .code(baseRole)
                        .name(toReadableName(baseRole))
                        .active(true)
                        .build();

                roles.add(role);
            }
        }

        repo.saveAll(roles);

        log.info("✔ RoleLoader: {} roles inserted.", roles.size());
    }

    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}
