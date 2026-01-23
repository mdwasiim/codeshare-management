package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.model.entities.Group;
import com.codeshare.airline.auth.model.entities.GroupMenu;
import com.codeshare.airline.auth.model.entities.Menu;
import com.codeshare.airline.auth.model.entities.Tenant;
import com.codeshare.airline.auth.repository.GroupMenuRepository;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMenuLoader {

    private final GroupMenuRepository groupMenuRepository;
    private final GroupRepository groupRepository;
    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;

    public void load(List<UUID> tenantIds) {

        if (menuRepository.count() > 0) {
            log.info("✔ GroupMenuLoader: group-menu mappings already exist — skipping.");
            return;
        }

        log.info("⏳ GroupMenuLoader: creating group-menu mappings...");

        List<GroupMenu> mappings = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            List<Group> groups =
                    groupRepository.findByTenant(tenant);

            List<Menu> menus = menuRepository.findByTenant(tenant);

            if (groups.isEmpty() || menus.isEmpty()) {
                log.warn("⚠ Skipping tenant {} — groups or menus missing", tenant.getTenantCode());
                continue;
            }

            for (Group group : groups) {

                List<Menu> allowedMenus = resolveMenusForGroup(group, menus);

                for (Menu menu : allowedMenus) {
                    mappings.add(
                            GroupMenu.builder()
                                    .tenant(tenant)
                                    .group(group)
                                    .menu(menu)
                                    .build()
                    );
                }
            }
        }

        groupMenuRepository.saveAll(mappings);

        log.info("✔ GroupMenuLoader: {} group-menu mappings created.", mappings.size());
    }

    /**
     * Decide which menus a group can see.
     * Adjust logic as per your business rules.
     */
    private List<Menu> resolveMenusForGroup(Group group, List<Menu> allMenus) {

        switch (group.getCode()) {

            case "ADMIN":
                return allMenus; // admin sees everything

            case "IT":
                return allMenus.stream()
                        .filter(m -> m.getCode().startsWith("IT_"))
                        .toList();

            case "OPS":
                return allMenus.stream()
                        .filter(m -> m.getCode().startsWith("OPS_"))
                        .toList();

            default:
                return Collections.emptyList();
        }
    }

    public boolean isLoaded() {
        return menuRepository.count() > 0;
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            log.warn("⚠ Invalid tenant UUID '{}'", id);
            return null;
        }
    }
}
