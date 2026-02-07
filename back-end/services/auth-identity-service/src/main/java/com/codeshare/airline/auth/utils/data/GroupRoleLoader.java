package com.codeshare.airline.auth.utils.data;


import com.codeshare.airline.auth.entities.Group;
import com.codeshare.airline.auth.entities.GroupRole;
import com.codeshare.airline.auth.entities.Role;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.GroupRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
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
    private static final Map<String, List<String>> GROUP_ROLE_MAP = Map.of(
            "ADMIN", List.of("ADMIN", "TENANT_ADMIN"),
            "IT", List.of("MANAGER"),
            "OPS", List.of("STAFF")
    );

    public void load(List<UUID> tenantIds) {

        log.info("⏳ GroupRoleLoader: ensuring group-role mappings...");

        List<GroupRole> toSave = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            List<Group> groups = groupRepository.findByTenant(tenant);
            List<Role> roles = roleRepository.findByTenant(tenant);

            if (groups.isEmpty() || roles.isEmpty()) {
                log.warn("⚠ Tenant {} missing groups or roles — skipping", tenant.getTenantCode());
                continue;
            }

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
        }

        if (!toSave.isEmpty()) {
            groupRoleRepository.saveAll(toSave);
            log.info("✔ GroupRoleLoader: {} mappings created.", toSave.size());
        } else {
            log.info("✔ GroupRoleLoader: mappings already exist.");
        }
    }

    public boolean isLoaded() {
        return groupRoleRepository.count()>0;
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid ssim ID '{}'", id);
            return null;
        }
    }
}
