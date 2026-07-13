package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.GroupRole;
import com.codeshare.airline.identity.access.assignments.repository.GroupRoleRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupRoleLoader {

    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final IdentityBootstrapData bootstrapData;

    public void load(Long tenantId) {
        log.info("GroupRoleLoader: ensuring group-role mappings for tenant {}", tenantId);

        List<Group> groups = groupRepository.findByTenantId(tenantId);
        List<Role> roles = roleRepository.findByTenantId(tenantId);

        Map<String, Group> groupByCode = new HashMap<>();
        groups.forEach(group -> groupByCode.put(group.getCode(), group));

        Map<String, Role> roleByCode = new HashMap<>();
        roles.forEach(role -> roleByCode.put(role.getCode(), role));

        Set<String> existingMappings = groupRoleRepository.findMappingsByTenantId(tenantId);
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
                        .tenantId(tenantId)
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

    public boolean isLoaded(Long tenantId) {
        long actual = groupRoleRepository.countByTenantId(tenantId);
        return actual >= bootstrapData.groupRoles().size();
    }
}
