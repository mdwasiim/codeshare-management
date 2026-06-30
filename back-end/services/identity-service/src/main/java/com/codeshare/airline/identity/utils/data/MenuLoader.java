package com.codeshare.airline.identity.utils.data;

import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.Tenant;
import com.codeshare.airline.identity.repository.MenuRepository;
import com.codeshare.airline.identity.repository.TenantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    private List<MenuDTO> loadMenuDefinitions() {

        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("bootstrap/menus.json")) {

            if (is == null) {
                throw new IllegalStateException("menus.json not found");
            }
            return objectMapper.readValue(
                    is,
                    new TypeReference<List<MenuDTO>>() {}
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to load menu definitions",
                    e
            );
        }
    }

    public void load(UUID tenantId) {

        log.info("MenuLoader: ensuring menus for tenant {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingCodes = menuRepository.findCodesByTenant(tenant);
        List<MenuDTO> menuDefinitions = loadMenuDefinitions();

        List<Menu> toSave = new ArrayList<>();
        List<Menu> toUpdate = new ArrayList<>();
        Map<String, Menu> codeMap = new HashMap<>();
        Map<String, Menu> allMenus = menuRepository.findByTenant(tenant)
                .stream()
                .collect(Collectors.toMap(Menu::getCode, m -> m));

        for (MenuDTO dto : menuDefinitions) {

            if (existingCodes.contains(dto.getCode())) {
                Menu existing = allMenus.get(dto.getCode());
                if (existing != null && updateMenuDefinition(existing, dto)) {
                    toUpdate.add(existing);
                }
                continue;
            }

            Menu menu = Menu.builder()
                    .code(dto.getCode())
                    .label(dto.getLabel())
                    .topbarLabel(dto.getTopbarLabel())
                    .sidebarLabel(dto.getSidebarLabel())
                    .icon(dto.getIcon())
                    .route(dto.getRoute())
                    .permission(dto.getPermission())
                    .displayOrder(dto.getDisplayOrder())
                    .tenant(tenant)
                    .build();

            toSave.add(menu);
            codeMap.put(dto.getCode(), menu);
        }

        allMenus.putAll(codeMap);

        for (MenuDTO dto : menuDefinitions) {
            if (dto.getParentCode() != null) {
                Menu child = allMenus.get(dto.getCode());
                Menu parent = allMenus.get(dto.getParentCode());

                if (child != null && parent != null && !Objects.equals(child.getParentMenu(), parent)) {
                    child.setParentMenu(parent);
                    if (!toSave.contains(child) && !toUpdate.contains(child)) {
                        toUpdate.add(child);
                    }
                }
            }
        }

        if (!toSave.isEmpty() || !toUpdate.isEmpty()) {
            List<Menu> changes = new ArrayList<>();
            changes.addAll(toSave);
            changes.addAll(toUpdate);

            menuRepository.saveAll(changes);
            log.info("MenuLoader: {} menus created and {} menus updated for {}", toSave.size(), toUpdate.size(), tenant.getTenantCode());
        }
    }

    private boolean updateMenuDefinition(Menu menu, MenuDTO dto) {
        boolean changed = false;

        if (!Objects.equals(menu.getLabel(), dto.getLabel())) {
            menu.setLabel(dto.getLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getTopbarLabel(), dto.getTopbarLabel())) {
            menu.setTopbarLabel(dto.getTopbarLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getSidebarLabel(), dto.getSidebarLabel())) {
            menu.setSidebarLabel(dto.getSidebarLabel());
            changed = true;
        }
        if (!Objects.equals(menu.getIcon(), dto.getIcon())) {
            menu.setIcon(dto.getIcon());
            changed = true;
        }
        if (!Objects.equals(menu.getRoute(), dto.getRoute())) {
            menu.setRoute(dto.getRoute());
            changed = true;
        }
        if (!Objects.equals(menu.getPermission(), dto.getPermission())) {
            menu.setPermission(dto.getPermission());
            changed = true;
        }
        if (!Objects.equals(menu.getDisplayOrder(), dto.getDisplayOrder())) {
            menu.setDisplayOrder(dto.getDisplayOrder());
            changed = true;
        }

        return changed;
    }

    public boolean isLoaded(UUID tenantId) {

        long actual = menuRepository.countByTenantId(tenantId);
        long expected = loadMenuDefinitions().size();

        return actual >= expected;
    }
}
