package com.codeshare.airline.identity.access.assignments.data;

import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.data.IdentityBootstrapData;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
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
    private final TenantRepository tenantRepository;
    private final IdentityBootstrapData bootstrapData;

    @Transactional
    public void load(UUID tenantId) {
        log.info("GroupMenuLoader: creating group-menu mappings for tenant {}", tenantId);
        assignTenantMenus(tenantId);
    }

    private void assignTenantMenus(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));

        List<Group> groups = groupRepository.findByTenant(tenant);
        List<Menu> menus = menuRepository.findByTenant(tenant);

        if (groups.isEmpty() || menus.isEmpty()) {
            log.warn("Skipping tenant [{}] because groups or menus are missing", tenant.getTenantCode());
            return;
        }

        Set<String> existingMappings = groupMenuRepository.findMappings(tenant);
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
                menus.forEach(menu -> saveGroupMenu(tenant, group, menu, existingMappings, toSave));
                return;
            }

            for (String menuCode : menuCodes) {
                Menu menu = menuByCode.get(menuCode);
                if (menu == null) {
                    log.warn("Menu [{}] not found for group [{}]", menuCode, groupCode);
                    continue;
                }

                saveGroupMenu(tenant, group, menu, existingMappings, toSave);
            }
        });

        if (!toSave.isEmpty()) {
            groupMenuRepository.saveAll(toSave);
            log.info("Tenant [{}]: {} group-menu mappings created.", tenant.getTenantCode(), toSave.size());
        } else {
            log.info("Tenant [{}]: group-menu mappings already exist.", tenant.getTenantCode());
        }
    }

    private void saveGroupMenu(Tenant tenant,
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
                .tenant(tenant)
                .group(group)
                .menu(menu)
                .build());
    }

    public boolean isLoaded(UUID tenantId) {
        long actual = groupMenuRepository.countByTenantId(tenantId);
        return actual >= bootstrapData.groupMenus().size();
    }
}
