package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Permission;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.PermissionRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionLoader {

    private final PermissionRepository permissionRepository;
    private final TenantRepository tenantRepository;

    // ===============================
    // 🔥 STANDARD ACTION SETS
    // ===============================
    private static final List<String> CRUD =
            List.of("create", "read", "update", "delete");

    private static final List<String> CRUD_EXTENDED =
            List.of("create", "read", "update", "delete", "export", "upload");

    private static final Map<String, List<String>> PERMISSION_DEFS = Map.ofEntries(

            // IAM
            Map.entry("user", CRUD_EXTENDED),
            Map.entry("group", CRUD),
            Map.entry("role", CRUD),
            Map.entry("permission", List.of("read", "assign", "revoke")),

            // BUSINESS
            Map.entry("booking", List.of("create", "read", "update", "delete", "cancel")),
            Map.entry("flight", List.of("create", "read", "update", "delete")),

            // REPORTING
            Map.entry("reports", List.of("read", "export")),

            // AUDIT
            Map.entry("audit", List.of("read")),

            // SYSTEM
            Map.entry("settings", CRUD),
            Map.entry("dashboard", List.of("read")),

            // COMMUNICATION
            Map.entry("notification", List.of("read", "create")),

            // FILE
            Map.entry("file", List.of("upload", "read", "delete"))
    );




    public void load(List<UUID> tenantIds) {

        log.info("⏳ PermissionLoader: ensuring permissions for {} tenants...", tenantIds.size());

        List<Permission> toSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            // 🔥 PERFORMANCE: load existing once
            Set<String> existingCodes = permissionRepository
                    .findCodesByTenant(tenant);

            PERMISSION_DEFS.forEach((domain, actions) -> {
                for (String action : actions) {

                    String code = domain + ":" + action;

                    if (existingCodes.contains(code)) continue;

                    toSave.add(
                            Permission.builder()
                                    .tenant(tenant)
                                    .code(code)
                                    .domain(domain)
                                    .action(action)
                                    .name(buildName(domain, action))
                                    .description(buildDescription(domain, action))
                                    .build()
                    );
                }
            });
        }

        if (!toSave.isEmpty()) {
            permissionRepository.saveAll(toSave);
            log.info("✅ PermissionLoader: {} permissions created.", toSave.size());
        } else {
            log.info("✅ PermissionLoader: all permissions already exist.");
        }
    }

    // ===============================
    // 🔥 TENANT-SPECIFIC CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long expected = PERMISSION_DEFS.values()
                .stream()
                .mapToLong(List::size)
                .sum();

        long actual = permissionRepository.countByTenantId(tenantId);

        return actual >= expected;
    }

    // ===============================
    // 🔥 HELPERS
    // ===============================
    private String buildName(String domain, String action) {
        return capitalize(domain) + " " + capitalize(action);
    }

    private String buildDescription(String domain, String action) {
        return "Allows user to " + action + " " + domain;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}