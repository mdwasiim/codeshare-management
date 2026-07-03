package com.codeshare.airline.identity.access.identity.data;

import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
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
public class GroupLoader {

    private final GroupRepository groupRepository;
    private final TenantRepository tenantRepository;

    private static final List<String> BASE_GROUPS = List.of(

            // PLATFORM / SECURITY
            "PLATFORM_TEAM",
            "SECURITY_TEAM",

            // TENANT ADMINISTRATION
            "TENANT_ADMIN_TEAM",

            // AIRLINE OPERATIONS
            "OPERATIONS_TEAM",
            "FLIGHT_OPERATIONS_TEAM",

            // BOOKING / RESERVATION
            "BOOKING_TEAM",

            // CUSTOMER SUPPORT
            "CUSTOMER_SUPPORT_TEAM",

            // REPORTING / AUDIT
            "AUDIT_TEAM",
            "ANALYTICS_TEAM",

            // TECHNICAL SUPPORT
            "IT_SUPPORT_TEAM",

            // DEFAULT USERS
            "DEFAULT_USERS"
    );

    public void load(UUID tenantId) {

        log.info("⏳ GroupLoader: ensuring base groups for {} tenants...", tenantId);

        List<Group> groupsToSave = new ArrayList<>();

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        // 🔥 PERFORMANCE FIX
        Set<String> existingCodes =
                groupRepository.findCodesByTenant(tenant);

        for (String code : BASE_GROUPS) {

            if (existingCodes.contains(code)) continue;

            groupsToSave.add(
                    Group.builder()
                            .tenant(tenant)
                            .code(code)
                            .name(toReadableName(code))
                            .description(buildDescription(code))
                            .build()
            );
        }

        if (!groupsToSave.isEmpty()) {
            groupRepository.saveAll(groupsToSave);
            log.info("✅ GroupLoader: {} groups created.", groupsToSave.size());
        } else {
            log.info("✅ GroupLoader: all groups already exist.");
        }
    }

    private String buildDescription(String code) {

        return switch (code) {

            case "PLATFORM_TEAM" ->
                    "Platform administration team";

            case "SECURITY_TEAM" ->
                    "Identity and security management team";

            case "TENANT_ADMIN_TEAM" ->
                    "Tenant administration team";

            case "OPERATIONS_TEAM" ->
                    "Airline operations management team";

            case "FLIGHT_OPERATIONS_TEAM" ->
                    "Flight operations and scheduling team";

            case "BOOKING_TEAM" ->
                    "Booking and reservation management team";

            case "CUSTOMER_SUPPORT_TEAM" ->
                    "Customer support and service team";

            case "AUDIT_TEAM" ->
                    "Audit and compliance team";

            case "ANALYTICS_TEAM" ->
                    "Reporting and analytics team";

            case "IT_SUPPORT_TEAM" ->
                    "Technical support and infrastructure team";

            case "DEFAULT_USERS" ->
                    "Default authenticated users group";

            default ->
                    code + " group";
        };
    }
    // ===============================
    // 🔥 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long actual = groupRepository.countByTenantId(tenantId);

        return actual >= 0;
    }

    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}

