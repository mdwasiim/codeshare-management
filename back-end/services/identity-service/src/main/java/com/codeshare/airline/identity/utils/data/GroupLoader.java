package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.GroupRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
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

            "ADMIN_GROUP",
            "TENANT_ADMIN_GROUP",
            "OPS_MANAGER_GROUP",
            "OPS_STAFF_GROUP",
            "BOOKING_TEAM",
            "FLIGHT_TEAM",
            "SUPPORT_TEAM",
            "AUDIT_TEAM",
            "IT_SUPPORT",
            "VIEWER_GROUP"
    );

    public void load(List<UUID> tenantIds) {

        log.info("⏳ GroupLoader: ensuring base groups for {} tenants...", tenantIds.size());

        List<Group> groupsToSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

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
                                .description(code + " for tenant " + tenant.getTenantCode())
                                .build()
                );
            }
        }

        if (!groupsToSave.isEmpty()) {
            groupRepository.saveAll(groupsToSave);
            log.info("✅ GroupLoader: {} groups created.", groupsToSave.size());
        } else {
            log.info("✅ GroupLoader: all groups already exist.");
        }
    }

    // ===============================
    // 🔥 TENANT-AWARE CHECK
    // ===============================
    public boolean isLoaded(UUID tenantId) {

        long expected = BASE_GROUPS.size();
        long actual = groupRepository.countByTenantId(tenantId);

        return actual >= expected;
    }

    private String toReadableName(String code) {
        return code.replace("_", " ");
    }
}

