package com.codeshare.airline.identity.service.serviceImpl;

import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.authentication.service.core.UserContextService;
import com.codeshare.airline.identity.entities.Group;
import com.codeshare.airline.identity.entities.Menu;
import com.codeshare.airline.identity.entities.User;
import com.codeshare.airline.identity.entities.UserGroup;
import com.codeshare.airline.identity.repository.GroupMenuRepository;
import com.codeshare.airline.identity.repository.MenuRepository;
import com.codeshare.airline.identity.service.MenuService;
import com.codeshare.airline.identity.utils.mappers.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final UserContextService userContextService;
    private final MenuRepository repository;
    private final GroupMenuRepository groupMenuRepository;
    private final MenuMapper mapper;

    // ---------------------------------------------------------
    // CREATE NEW MENU FOR TENANT
    // ---------------------------------------------------------
    @Override
    public MenuDTO create(MenuDTO dto) {

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        Menu entity = mapper.toEntity(dto);

        // Assign parent if exists
        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        }

        return mapper.toDTO(repository.save(entity));
    }


    // ---------------------------------------------------------
    // UPDATE MENU DETAILS
    // ---------------------------------------------------------
    @Override
    public MenuDTO update(UUID id, MenuDTO dto) {

        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        // 🔥 Use mapper
        mapper.updateEntityFromDto(dto, entity);

        // 🔥 Handle parent manually
        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        } else {
            entity.setParentMenu(null);
        }

        return mapper.toDTO(repository.save(entity));
    }


    // ---------------------------------------------------------
    // GET MENU BY ID
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public MenuDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }


    // ---------------------------------------------------------
    // GET TOP-LEVEL (ROOT) MENUS FOR TENANT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getRootMenus(UUID tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndParentMenuIsNull(tenantId));
    }


    // ---------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // ---------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllByTenant(String tenantCode) {

        User user = userContextService.getCurrentUser();

        List<Group> groups = user.getUserGroups()
                .stream()
                .map(UserGroup::getGroup)
                .toList();

        // 🔥 fetch allowed menus
        List<Menu> allowedMenus =
                groupMenuRepository.findMenusByGroupsAndTenant(groups, tenantCode);

        // 🔥 include parents
        Set<Menu> allowedSet = new HashSet<>(allowedMenus);

        for (Menu menu : allowedMenus) {
            Menu parent = menu.getParentMenu();
            while (parent != null) {
                allowedSet.add(parent);
                parent = parent.getParentMenu();
            }
        }

        allowedSet.forEach(menu -> {
            if (menu.getParentMenu() != null) {
                menu.getParentMenu().getId();
            }
        });

        // ✅ RETURN FLAT LIST
        return allowedSet.stream()
                .sorted(Comparator.comparing(Menu::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)))
                .map(mapper::toDTO)
                .toList();
    }

    // ---------------------------------------------------------
    // DELETE MENU BY ID
    // ---------------------------------------------------------
    @Override
    public void delete(UUID id) {
        Menu menu = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        repository.delete(menu);
    }
}
