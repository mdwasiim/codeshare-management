package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.integration.tenant.HostAirlineTenantClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMenuLoader {

    private final GroupMenuRepository groupMenuRepository;
    private final GroupRepository groupRepository;
    private final MenuRepository menuRepository;
    private final IdentityBootstrapData bootstrapData;
    private final HostAirlineTenantClient tenantClient;

    @Transactional
    public void load(UUID tenantId) {
        log.info("GroupMenuLoader: creating group-menu mappings for tenant {}", tenantId);
        assignTenantMenus(tenantId);
    }

    private void assignTenantMenus(UUID tenantId) {
        List<Group> groups = groupRepository.findByTenantId(tenantId);
        List<Menu> menus = menuRepository.findByTenantId(tenantId);
        String tenantCode = resolveTenantCode(tenantId);

        if (groups.isEmpty() || menus.isEmpty()) {
            log.warn("Skipping tenant [{}] because groups or menus are missing", tenantCode);
            return;
        }

        Set<String> existingMappings = groupMenuRepository.findMappingsByTenantId(tenantId);
        List<GroupMenu> toSave = new ArrayList<>();

        Map<String, Group> groupByCode = new HashMap<>();
        groups.forEach(group -> groupByCode.put(group.getCode(), group));

        Map<String, Menu> menuByCode = new HashMap<>();
        menus.forEach(menu -> menuByCode.put(menu.getCode(), menu));

        bootstrapData.groupMenus().forEach((groupCode, menuCodes) -> {
            Group group = groupByCode.get(groupCode);
            if (group == null) {
                log.warn("Group [{}] not found for group-menu bootstrap", groupCode);
                return;
            }

            if (menuCodes.contains("*")) {
                menus.forEach(menu -> saveGroupMenu(tenantId, group, menu, existingMappings, toSave));
                return;
            }

            for (String menuCode : menuCodes) {
                Menu menu = menuByCode.get(menuCode);
                if (menu == null) {
                    log.warn("Menu [{}] not found for group [{}]", menuCode, groupCode);
                    continue;
                }

                saveGroupMenu(tenantId, group, menu, existingMappings, toSave);
            }
        });

        if (!toSave.isEmpty()) {
            groupMenuRepository.saveAll(toSave);
            log.info("Tenant [{}]: {} group-menu mappings created.", tenantCode, toSave.size());
        } else {
            log.info("Tenant [{}]: group-menu mappings already exist.", tenantCode);
        }
    }

    private void saveGroupMenu(UUID tenantId,
                               Group group,
                               Menu menu,
                               Set<String> existingMappings,
                               List<GroupMenu> toSave) {
        String key = group.getCode() + ":" + menu.getCode();
        if (existingMappings.contains(key)) {
            return;
        }

        existingMappings.add(key);
        toSave.add(GroupMenu.builder()
                .tenantId(tenantId)
                .group(group)
                .menu(menu)
                .build());
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = groupMenuRepository.countByTenantId(tenantId);
        return actual >= bootstrapData.groupMenus().size();
    }

    private String resolveTenantCode(UUID tenantId) {
        return tenantClient.getAll().stream()
                .filter(tenant -> tenantId.equals(tenant.getId()))
                .map(tenant -> tenant.getTenantCode())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Tenant not found in tenant-service: " + tenantId));
    }
}
