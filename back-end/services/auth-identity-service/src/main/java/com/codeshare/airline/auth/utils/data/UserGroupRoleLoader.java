package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.rbac.Group;
import com.codeshare.airline.auth.entities.rbac.GroupRole;
import com.codeshare.airline.auth.entities.rbac.UserGroupRole;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.UserGroupRoleRepository;
import com.codeshare.airline.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGroupRoleLoader {

    private final UserRepository userRepo;
    private final GroupRepository groupRepo;
    private final UserGroupRoleRepository repo;

    @Transactional
    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ UserGroupRoleLoader already present — skipping load.");
            return;
        }

        for (String t : tenantIds) {
            UUID tenantId = safeUUID(t);
            if (tenantId == null) continue;
            loadForTenant(tenantId);
        }
    }

    private void loadForTenant(UUID tenantId) {

        List<User> users = userRepo.findByTenantId(tenantId);
        List<Group> groups = groupRepo.findByTenantId(tenantId);

        if (users.isEmpty() || groups.isEmpty()) {
            log.warn("⚠ Tenant {}: No users or groups found — skipping", tenantId);
            return;
        }

        List<UserGroupRole> userGroupRoles = new ArrayList<>();

        for (User user : users) {
            for (Group group : groups) {

                Set<GroupRole> groupRoles = group.getGroupRoles();
                if (groupRoles == null || groupRoles.isEmpty()) continue;

                for (GroupRole groupRole : groupRoles) {

                    userGroupRoles.add(
                            UserGroupRole.builder()
                                    .tenantId(tenantId)
                                    .user(user)
                                    .group(group)
                                    .role(groupRole.getRole())
                                    .build()
                    );
                }
            }
        }

        if (!userGroupRoles.isEmpty()) {
            repo.saveAll(userGroupRoles);
            log.info("✔ Tenant {}: {} user-group-role mappings created.",
                    tenantId, userGroupRoles.size());
        }
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            log.warn("Invalid tenant UUID: {}", id);
            return null;
        }
    }
}
