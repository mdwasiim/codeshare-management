package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.rbac.Permission;
import com.codeshare.airline.auth.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionLoader {

    private final PermissionRepository repo;

    /**
     * Modular RBAC: Domain → Actions
     *
     * Each domain can have multiple actions.
     * The final permission code becomes: "domain:action"
     */
    private static final Map<String, List<String>> PERMISSION_DEFS = Map.of(
            "user", List.of("create", "update", "delete"),
            "group", List.of("create", "update", "delete"),
            "role", List.of("create", "update"),
            "permission", List.of("assign", "revoke"),
            "booking", List.of("manage", "cancel"),
            "flight", List.of("manage", "update"),
            "reports", List.of("generate"),
            "audit", List.of("view")
    );

    /**
     * Loads permissions for all tenants
     */
    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ PermissionLoader: Permissions already exist — skipping load.");
            return;
        }

        log.info("⏳ PermissionLoader: Initializing permissions for {} tenants…", tenantIds.size());

        List<Permission> permissionsToInsert = new ArrayList<>();

        for (String tenantIdStr : tenantIds) {

            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId == null) continue;

            log.info("→ Creating permissions for tenant {}", tenantId);

            PERMISSION_DEFS.forEach((domain, actions) -> {
                for (String action : actions) {

                    String code = domain + ":" + action;

                    Permission permission = Permission.builder()
                            .tenantId(tenantId)
                            .code(code)
                            .domain(domain)
                            .action(action)
                            .name(buildName(domain, action))                     // e.g. "User Create"
                            .description("Allows " + action + " operation on " + domain)
                            .active(true)
                            .build();

                    permissionsToInsert.add(permission);
                }
            });
        }

        repo.saveAll(permissionsToInsert);

        log.info("✔ PermissionLoader: {} permissions inserted successfully.", permissionsToInsert.size());
    }

    /** Helper: Converts "user", "create" → "User Create" */
    private String buildName(String domain, String action) {
        return capitalize(domain) + " " + capitalize(action);
    }

    /** Capitalize first letter */
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /** UUID safety */
    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid tenant UUID '{}' — skipping...", id);
            return null;
        }
    }
}
