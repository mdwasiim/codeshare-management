package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.assignments.repository.GroupRoleRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupRoleLoader {

    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(UUID tenantId) {
        log.info("GroupRoleLoader: ensuring group-role mappings for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        List<Group> groups = groupRepository.findByTenant(tenant);
        List<Role> roles = roleRepository.findByTenant(tenant);

        Map<String, Group> groupByCode = new HashMap<>();
        groups.forEach(group -> groupByCode.put(group.getCode(), group));

        Map<String, Role> roleByCode = new HashMap<>();
        roles.forEach(role -> roleByCode.put(role.getCode(), role));

        Set<String> existingMappings = groupRoleRepository.findMappings(tenant);
        List<GroupRole> toSave = new ArrayList<>();
        bootstrapData.groupRoles().forEach((groupCode, roleCodes) -> {
            Group group = groupByCode.get(groupCode);
            if (group == null) {
                log.warn("Group [{}] not found for group-role bootstrap", groupCode);
                return;
            }

            for (String roleCode : roleCodes) {
                Role role = roleByCode.get(roleCode);
                if (role == null) {
                    log.warn("Role [{}] not found for group [{}]", roleCode, groupCode);
                    continue;
                }

                String key = group.getCode() + ":" + role.getCode();
                if (existingMappings.contains(key)) {
                    continue;
                }

                existingMappings.add(key);
                toSave.add(GroupRole.builder()
                        .tenant(tenant)
                        .group(group)
                        .role(role)
                        .build());
            }
        });

        if (!toSave.isEmpty()) {
            groupRoleRepository.saveAll(toSave);
            log.info("GroupRoleLoader: {} mappings created.", toSave.size());
        } else {
            log.info("GroupRoleLoader: mappings already exist.");
        }
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = groupRoleRepository.countByTenantId(tenantId);
        return actual >= bootstrapData.groupRoles().size();
    }
}
