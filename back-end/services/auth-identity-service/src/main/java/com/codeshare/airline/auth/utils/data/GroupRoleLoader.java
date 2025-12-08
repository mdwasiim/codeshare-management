package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.entities.rbac.GroupRole;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.GroupRoleRepository;
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
public class GroupRoleLoader {

    private final GroupRepository groupRepo;
    private final RoleRepository roleRepo;
    private final GroupRoleRepository repo;

    /**
     * Loads GroupRole mappings for all tenants provided by DataLoader.
     */
    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ GroupRoleLoader: Mappings already exist — skipping load.");
            return;
        }

        log.info("⏳ GroupRoleLoader: Assigning roles to groups for {} tenants...", tenantIds.size());

        int totalCount = 0;

        for (String tenantIdStr : tenantIds) {
            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId != null) {
                totalCount += assignRolesToGroups(tenantId);
            }
        }

        log.info("✔ GroupRoleLoader: Completed. Total GroupRole records inserted: {}", totalCount);
    }

    /**
     * Assigns ALL roles of a tenant to ALL groups of that tenant.
     */
    private int assignRolesToGroups(UUID tenantId) {

        List<Group> groups = groupRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (groups.isEmpty()) {
            log.warn("⚠ No groups found for tenant {} — skipping group-role mapping", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping group-role mapping", tenantId);
            return 0;
        }

        List<GroupRole> assignments = new ArrayList<>();

        for (Group group : groups) {
            for (Role role : roles) {
                assignments.add(
                        GroupRole.builder()
                                .tenantId(tenantId)
                                .group(group)
                                .role(role)
                                .build()
                );
            }
        }

        repo.saveAll(assignments);

        log.info("✔ Tenant {}: {} GroupRole mappings created.", tenantId, assignments.size());

        return assignments.size();
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid tenant ID '{}', skipping...", id);
            return null;
        }
    }
}
