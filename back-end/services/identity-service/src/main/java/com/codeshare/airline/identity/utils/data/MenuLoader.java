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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuLoader {

    private final MenuRepository menuRepository;
    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    // Preload JSON once
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

        log.info("⏳ MenuLoader: ensuring menus for {} tenants...", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalStateException("Tenant not found: " + tenantId));

        Set<String> existingCodes = menuRepository.findCodesByTenant(tenant);

        List<Menu> toSave = new ArrayList<>();

        Map<String, Menu> codeMap = new HashMap<>();

        List<MenuDTO> MENU_DEFS = loadMenuDefinitions();
        // 1. Create missing menus
        for (MenuDTO dto : MENU_DEFS) {

            if (existingCodes.contains(dto.getCode())) continue;

            Menu menu = Menu.builder()
                    .code(dto.getCode())
                    .label(dto.getLabel())
                    .icon(dto.getIcon())
                    .route(dto.getRoute())
                    .permission(dto.getPermission())
                    .displayOrder(dto.getDisplayOrder())
                    .tenant(tenant)
                    .build();

            toSave.add(menu);
            codeMap.put(dto.getCode(), menu);
        }

        // 2. Fetch already existing menus (for parent mapping)
        Map<String, Menu> allMenus = menuRepository.findByTenant(tenant)
                .stream()
                .collect(Collectors.toMap(Menu::getCode, m -> m));

        allMenus.putAll(codeMap);

        // 3. Assign parents
        for (MenuDTO dto : MENU_DEFS) {
            if (dto.getParentCode() != null) {
                Menu child = allMenus.get(dto.getCode());
                Menu parent = allMenus.get(dto.getParentCode());

                if (child != null && parent != null) {
                    child.setParentMenu(parent);
                }
            }
        }

        if (!toSave.isEmpty()) {
            menuRepository.saveAll(toSave);
            log.info("✅ MenuLoader: {} menus created for {}", toSave.size(), tenant.getTenantCode());
        }
    }

    public boolean isLoaded(UUID tenantId) {

        long actual = menuRepository.countByTenantId(tenantId);

        return actual >= 0;
    }

}
