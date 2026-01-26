package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Group;
import com.codeshare.airline.auth.entities.GroupMenu;
import com.codeshare.airline.auth.entities.Menu;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.GroupMenuRepository;
import com.codeshare.airline.auth.repository.GroupRepository;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public void load(List<UUID> tenantIds) {

        if (groupMenuRepository.count() > 0) {
            log.info("✔ GroupMenuLoader: group-menu mappings already exist — skipping.");
            return;
        }

        log.info("⏳ GroupMenuLoader: creating group-menu mappings...");

        List<GroupMenu> mappings = new ArrayList<>();

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() ->new IllegalStateException("Tenant not found: " + tenantId));

            List<Group> groups = groupRepository.findByTenant(tenant);
            List<Menu> menus = menuRepository.findByTenant(tenant);

            if (groups.isEmpty() || menus.isEmpty()) {
                log.warn("⚠ Skipping tenant {} — groups or menus missing", tenant.getTenantCode());
                continue;
            }

            for (Group group : groups) {

                for (Menu menu : menus) {
                    mappings.add(GroupMenu.builder()
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

    public boolean isLoaded() {
        return groupMenuRepository.count() > 0;
    }
}
