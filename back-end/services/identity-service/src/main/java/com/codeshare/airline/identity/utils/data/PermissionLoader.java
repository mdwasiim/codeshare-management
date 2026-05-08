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
    private static final Map<String, List<String>> PERMISSION_DEFS = Map.ofEntries(

            // =========================
            // IAM
            // =========================
            Map.entry("user",
                    List.of("create", "read", "update", "delete", "unlock")),

            Map.entry("group",
                    List.of("create", "read", "update", "delete", "assign")),

            Map.entry("role",
                    List.of("create", "read", "update", "delete", "assign")),

            Map.entry("permission",
                    List.of("read", "assign")),

            // =========================
            // MENU / UI
            // =========================
            Map.entry("menu",
                    List.of("create", "read", "update", "delete")),

            Map.entry("dashboard",
                    List.of("read")),

            // =========================
            // AIRLINE OPERATIONS
            // =========================
            Map.entry("flight",
                    List.of("create", "read", "update", "cancel")),

            Map.entry("booking",
                    List.of("create", "read", "update", "cancel", "refund")),

            // =========================
            // REPORTING / AUDIT
            // =========================
            Map.entry("report",
                    List.of("read", "export")),

            Map.entry("audit",
                    List.of("read")),

            // =========================
            // SYSTEM
            // =========================
            Map.entry("settings",
                    List.of("read", "update")),

            Map.entry("notification",
                    List.of("create", "read", "send")),

            // =========================
            // FILE MANAGEMENT
            // =========================
            Map.entry("file",
                    List.of("upload", "read", "delete"))
    );




    public void load(UUID tenantId) {

        log.info("⏳ PermissionLoader: ensuring permissions for {} tenants...", tenantId);

        List<Permission> toSave = new ArrayList<>();

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        // 🔥 PERFORMANCE: load existing once
        Set<String> existingCodes = permissionRepository
                .findCodesByTenant(tenant);

        PERMISSION_DEFS.forEach((domain, actions) -> {

            for (String action : actions) {

                String code = (domain + ":" + action).toLowerCase();

                if (existingCodes.contains(code)) {
                    continue;
                }
                // prevent duplicates in current batch
                existingCodes.add(code);

                toSave.add(
                        Permission.builder()
                                .tenant(tenant)
                                .name(buildName(domain, action))
                                .code(code)
                                .domain(domain)
                                .action(action)
                                .description(buildDescription(domain, action))
                                .build()
                );
            }
        });

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
        long actual = permissionRepository.countByTenantId(tenantId);

        return actual >= 0;
    }

    // ===============================
    // 🔥 HELPERS
    // ===============================
    private String buildName(String domain, String action) {
        return capitalize(action) + " " + capitalize(domain);
    }

    private String buildDescription(String domain, String action) {

        return switch (action.toLowerCase()) {

            case "create" ->
                    "Allows creating " + domain + " records";

            case "read" ->
                    "Allows viewing " + domain + " records";

            case "update" ->
                    "Allows updating " + domain + " records";

            case "delete" ->
                    "Allows deleting " + domain + " records";

            case "assign" ->
                    "Allows assigning " + domain;

            case "export" ->
                    "Allows exporting " + domain + " data";

            case "upload" ->
                    "Allows uploading " + domain + " data";

            case "cancel" ->
                    "Allows cancelling " + domain;

            case "refund" ->
                    "Allows processing refunds";

            case "unlock" ->
                    "Allows unlocking users";

            case "send" ->
                    "Allows sending notifications";

            default ->
                    "Allows " + action + " access on " + domain;
        };
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}