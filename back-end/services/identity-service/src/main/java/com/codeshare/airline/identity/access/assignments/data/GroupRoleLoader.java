package com.codeshare.airline.identity.access.assignments.data;


import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.assignments.repository.GroupRoleRepository;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupRoleLoader {

    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final TenantRepository tenantRepository;

    /**
     * Default role assignments per group
     */
    private static final Map<String, List<String>> GROUP_ROLE_MAP = Map.ofEntries(

            // =========================
            // PLATFORM / SECURITY
            // =========================
            Map.entry("PLATFORM_TEAM",
                    List.of("SUPER_ADMIN")),

            Map.entry("SECURITY_TEAM",
                    List.of("IAM_ADMIN")),

            // =========================
            // TENANT ADMIN
            // =========================
            Map.entry("TENANT_ADMIN_TEAM",
                    List.of("TENANT_ADMIN")),

            // =========================
            // OPERATIONS
            // =========================
            Map.entry("OPERATIONS_TEAM",
                    List.of("OPS_MANAGER")),

            Map.entry("FLIGHT_OPERATIONS_TEAM",
                    List.of("FLIGHT_OPERATOR")),

            // =========================
            // BOOKING
            // =========================
            Map.entry("BOOKING_TEAM",
                    List.of("BOOKING_AGENT")),

            // =========================
            // CUSTOMER SUPPORT
            // =========================
            Map.entry("CUSTOMER_SUPPORT_TEAM",
                    List.of("CUSTOMER_SUPPORT")),

            // =========================
            // REPORTING / AUDIT
            // =========================
            Map.entry("AUDIT_TEAM",
                    List.of("AUDITOR")),

            Map.entry("ANALYTICS_TEAM",
                    List.of("REPORT_ANALYST")),

            // =========================
            // IT SUPPORT
            // =========================
            Map.entry("IT_SUPPORT_TEAM",
                    List.of("IAM_ADMIN")),

            // =========================
            // DEFAULT USERS
            // =========================
            Map.entry("DEFAULT_USERS",
                    List.of("USER"))
    );

    public void load(UUID tenantId) {

        log.info("⏳ GroupRoleLoader: ensuring group-role mappings...");

        List<GroupRole> toSave = new ArrayList<>();


        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        List<Group> groups = groupRepository.findByTenant(tenant);
        List<Role> roles = roleRepository.findByTenant(tenant);

        Map<String, Role> roleByCode = new HashMap<>();
        for (Role role : roles) {
            roleByCode.put(role.getCode(), role);
        }

        for (Group group : groups) {

            List<String> allowedRoles =
                    GROUP_ROLE_MAP.getOrDefault(group.getCode(), List.of());

            for (String roleCode : allowedRoles) {

                Role role = roleByCode.get(roleCode);
                if (role == null) continue;

                boolean exists =
                        groupRoleRepository.existsByTenantAndGroupAndRole(
                                tenant, group, role);

                if (exists) continue;

                toSave.add(
                        GroupRole.builder()
                                .tenant(tenant)
                                .group(group)
                                .role(role)
                                .build()
                );
            }
        }

        if (!toSave.isEmpty()) {
            groupRoleRepository.saveAll(toSave);
            log.info("GroupRoleLoader: {} mappings created.", toSave.size());
        } else {
            log.info(" GroupRoleLoader: mappings already exist.");
        }
    }

    public boolean isLoaded(UUID tenantId) {

        long actual = groupRoleRepository.countByTenantId(tenantId);

        return actual >= 0;
    }

}
