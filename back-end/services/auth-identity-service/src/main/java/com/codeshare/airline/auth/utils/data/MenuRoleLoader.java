package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.MenuRoleRepository;
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
public class MenuRoleLoader {

    private final MenuRepository menuRepo;
    private final RoleRepository roleRepo;
    private final MenuRoleRepository repo;

    public void load(List<String> tenantIds) {

        if (repo.count() > 0) {
            log.info("✔ MenuRoleLoader: Menu-role mappings already exist — skipping load.");
            return;
        }

        log.info("⏳ MenuRoleLoader: Assigning menus to roles for {} tenants...", tenantIds.size());

        for (String tenantIdStr : tenantIds) {

            UUID tenantId = safeUUID(tenantIdStr);
            if (tenantId == null) continue;

            int assigned = linkTenantMenus(tenantId);

            log.info("✔ Tenant {}: {} menu-role mappings created.", tenantId, assigned);
        }

        log.info("✔ MenuRoleLoader: Completed assigning menu-role mappings.");
    }

    private int linkTenantMenus(UUID tenantId) {

        List<Menu> menus = menuRepo.findByTenantId(tenantId);
        List<Role> roles = roleRepo.findByTenantId(tenantId);

        if (menus.isEmpty()) {
            log.warn("⚠ No menus found for tenant {} — skipping menu-role assignment", tenantId);
            return 0;
        }

        if (roles.isEmpty()) {
            log.warn("⚠ No roles found for tenant {} — skipping menu-role assignment", tenantId);
            return 0;
        }

        List<MenuRole> mappings = new ArrayList<>();

        for (Menu menu : menus) {
            for (Role role : roles) {
                mappings.add(MenuRole.builder()
                        .tenantId(tenantId)
                        .menu(menu)
                        .role(role)
                        .build());
            }
        }

        repo.saveAll(mappings);

        return mappings.size();
    }

    private UUID safeUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            log.warn("⚠ Invalid tenant UUID '{}' — skipping...", id);
            return null;
        }
    }
}
