package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Permission;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.PermissionRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
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

    private final PermissionRepository permissionRepository;
    private final TenantRepository tenantRepository;

    /**
     * Domain → actions
     * Final permission code = domain:action
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

    public void load(List<UUID> tenantIds) {

        log.info("⏳ PermissionLoader: ensuring permissions for {} tenants...", tenantIds.size());

        List<Permission> toSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            PERMISSION_DEFS.forEach((domain, actions) -> {
                for (String action : actions) {

                    String code = domain + ":" + action;

                    boolean exists =
                            permissionRepository.existsByTenantAndCode(tenant, code);

                    if (exists) {
                        continue;
                    }

                    toSave.add(
                            Permission.builder()
                                    .tenant(tenant)
                                    .code(code)
                                    .domain(domain)
                                    .action(action)
                                    .name(buildName(domain, action))
                                    .description("Allows " + action + " operation on " + domain)
                                    .build()
                    );
                }
            });
        }

        if (!toSave.isEmpty()) {
            permissionRepository.saveAll(toSave);
            log.info("✔ PermissionLoader: {} permissions created.", toSave.size());
        } else {
            log.info("✔ PermissionLoader: all permissions already exist.");
        }
    }

    /**
     * Tenant-aware initialization check
     */
    public boolean isLoaded() {
        return permissionRepository.count()>0;
    }

    private String buildName(String domain, String action) {
        return capitalize(domain) + " " + capitalize(action);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid ingestion UUID '{}'", id);
            return null;
        }
    }
}
