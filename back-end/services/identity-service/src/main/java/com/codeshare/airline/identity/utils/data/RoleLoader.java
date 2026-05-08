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

            // PLATFORM
            "SUPER_ADMIN",

            // TENANT
            "TENANT_ADMIN",

            // IAM / SECURITY
            "IAM_ADMIN",

            // AIRLINE OPERATIONS
            "OPS_MANAGER",
            "FLIGHT_OPERATOR",

            // BOOKING / RESERVATION
            "BOOKING_AGENT",

            // CUSTOMER SUPPORT
            "CUSTOMER_SUPPORT",

            // REPORTING / AUDIT
            "REPORT_ANALYST",
            "AUDITOR",

            // DEFAULT BUSINESS USER
            "USER"
    );

    public void load(UUID tenantId) {

        log.info("⏳ RoleLoader: ensuring base roles for {} tenants...", tenantId);

        List<Role> rolesToSave = new ArrayList<>();

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
                            .description(buildDescription(code))
                            .build()
            );
        }


        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
            log.info("✅ RoleLoader: {} roles created.", rolesToSave.size());
        } else {
            log.info("✅ RoleLoader: all roles already exist.");
        }
    }
    private String buildDescription(String code) {

        return switch (code) {

            case "SUPER_ADMIN" ->
                    "Full platform administration access";

            case "TENANT_ADMIN" ->
                    "Tenant administration and configuration access";

            case "IAM_ADMIN" ->
                    "Identity and access management administration";

            case "OPS_MANAGER" ->
                    "Operational management access";

            case "FLIGHT_OPERATOR" ->
                    "Flight operations management access";

            case "BOOKING_AGENT" ->
                    "Booking and reservation management access";

            case "CUSTOMER_SUPPORT" ->
                    "Customer support operations access";

            case "REPORT_ANALYST" ->
                    "Reporting and analytics access";

            case "AUDITOR" ->
                    "Audit and compliance read-only access";

            case "USER" ->
                    "Default authenticated user access";

            default ->
                    code + " role";
        };
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