package com.codeshare.airline.auth.utils.data;

import com.codeshare.airline.auth.entities.Menu;
import com.codeshare.airline.auth.entities.Tenant;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.TenantRepository;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    // Preload JSON once
    private static final String MENU_JSON ="[{\"label\":\"Home\",\"displayOrder\":1,\"items\":[{\"label\":\"Dashboard\",\"icon\":\"pi pi-fw pi-home\",\"displayOrder\":1,\"routerLink\":[\"/\"]}]},{\"label\":\"Settings\",\"icon\":\"pi pi-fw pi-briefcase\",\"displayOrder\":2,\"items\":[{\"label\":\"Organization\",\"icon\":\"pi pi-fw pi-globe\",\"displayOrder\":1,\"items\":[{\"label\":\"All Organizations\",\"icon\":\"pi pi-fw pi-list\",\"displayOrder\":1,\"routerLink\":[\"/organizations\"]},{\"label\":\"Create Organization\",\"icon\":\"pi pi-fw pi-plus\",\"displayOrder\":2,\"routerLink\":[\"/organizations/create\"]}]}]},{\"label\":\"Pages\",\"icon\":\"pi pi-fw pi-briefcase\",\"displayOrder\":3,\"routerLink\":[\"/pages\"],\"items\":[{\"label\":\"Landing\",\"icon\":\"pi pi-fw pi-globe\",\"displayOrder\":1,\"routerLink\":[\"/landing\"]},{\"label\":\"Auth\",\"icon\":\"pi pi-fw pi-user\",\"displayOrder\":2,\"items\":[{\"label\":\"Login\",\"icon\":\"pi pi-fw pi-sign-in\",\"displayOrder\":1,\"routerLink\":[\"/auth/login\"]},{\"label\":\"Error\",\"icon\":\"pi pi-fw pi-times-circle\",\"displayOrder\":2,\"routerLink\":[\"/auth/error\"]},{\"label\":\"Access Denied\",\"icon\":\"pi pi-fw pi-lock\",\"displayOrder\":3,\"routerLink\":[\"/auth/access\"]}]},{\"label\":\"Product\",\"icon\":\"pi pi-fw pi-pencil\",\"displayOrder\":3,\"routerLink\":[\"/pages/product\"]},{\"label\":\"Not Found\",\"icon\":\"pi pi-fw pi-exclamation-circle\",\"displayOrder\":4,\"routerLink\":[\"/pages/notfound\"]},{\"label\":\"Empty\",\"icon\":\"pi pi-fw pi-circle-off\",\"displayOrder\":5,\"routerLink\":[\"/pages/empty\"]}]},{\"label\":\"Hierarchy\",\"displayOrder\":4,\"items\":[{\"label\":\"Submenu 1\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":1,\"items\":[{\"label\":\"Submenu 1.1\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":1,\"items\":[{\"label\":\"Submenu 1.1.1\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":1}]}]},{\"label\":\"Submenu 2\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":2,\"items\":[{\"label\":\"Submenu 2.1\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":1,\"items\":[{\"label\":\"Submenu 2.1.1\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":1},{\"label\":\"Submenu 2.1.2\",\"icon\":\"pi pi-fw pi-bookmark\",\"displayOrder\":2}]}]}]}]";


    public void load(List<UUID> tenantIds) {

        log.info("⏳ MenuLoader: ensuring menus for {} tenants...", tenantIds.size());

        for (UUID tenantId : tenantIds) {

            if (tenantId == null) continue;

            Tenant tenant = tenantRepository.findById(tenantId)
                    .orElseThrow(() ->
                            new IllegalStateException("Tenant not found: " + tenantId));

            if (menuRepository.existsByTenant(tenant)) {
                log.info("✔ Menus already exist for ingestion {}", tenant.getTenantCode());
                continue;
            }

            List<Menu> menus = buildMenusForTenant(tenant);
            menuRepository.saveAll(menus);

            log.info("✔ MenuLoader: {} menus created for ingestion {}", menus.size(), tenant.getTenantCode());
        }
    }

    public boolean isLoaded() {
        return menuRepository.count()>0;
    }

    private List<Menu> buildMenusForTenant(Tenant tenant) {

        List<MenuDTO> rootMenus;
        try {
            rootMenus = objectMapper.readValue(MENU_JSON, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse menu JSON", e);
        }

        List<Menu> collector = new ArrayList<>();

        for (MenuDTO dto : rootMenus) {
            processMenu(dto, null, tenant, collector);
        }

        return collector;
    }

    private void processMenu(
            MenuDTO dto,
            Menu parent,
            Tenant tenant,
            List<Menu> collector
    ) {
        Menu menu = Menu.builder()
                .label(dto.getLabel())
                .icon(dto.getIcon())
                .routerLink(dto.getRouterLink())
                .displayOrder(dto.getDisplayOrder())
                .tenant(tenant)
                .icon(dto.getIcon())
                .parentMenu(parent)
                .build();

        collector.add(menu);

        if (dto.getItems()!= null) {
            for (MenuDTO child : dto.getItems()) {
                processMenu(child, menu, tenant, collector);
            }
        }
    }
}
