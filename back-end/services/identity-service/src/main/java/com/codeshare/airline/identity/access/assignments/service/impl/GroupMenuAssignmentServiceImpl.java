package com.codeshare.airline.identity.access.assignments.service.impl;

import com.codeshare.airline.core.dto.tenant.GroupMenuDTO;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.identity.access.assignments.entities.GroupMenu;
import com.codeshare.airline.identity.access.authorization.entities.Menu;
import com.codeshare.airline.identity.access.assignments.repository.GroupMenuRepository;
import com.codeshare.airline.identity.access.identity.repository.GroupRepository;
import com.codeshare.airline.identity.access.authorization.repository.MenuRepository;
import com.codeshare.airline.identity.access.assignments.service.GroupMenuAssignmentService;
import com.codeshare.airline.identity.access.assignments.mappers.GroupMenuMapper;
import com.codeshare.airline.identity.access.authorization.mappers.MenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupMenuAssignmentServiceImpl
        implements GroupMenuAssignmentService {

    private final GroupRepository groupRepository;

    private final MenuRepository menuRepository;

    private final GroupMenuRepository groupMenuRepository;

    private final MenuMapper menuMapper;

    private final GroupMenuMapper groupMenuMapper;

    // =====================================================
    // GET MENUS BY GROUP
    // =====================================================
    @Transactional(readOnly = true)
    @Override
    public List<MenuDTO> getMenusByGroup(UUID groupId) {

        log.info("Fetching menus for groupId: {}", groupId);

        List<GroupMenu> groupMenus =
                groupMenuRepository.findByGroup_Id(groupId);

        List<MenuDTO> menus = groupMenus.stream()
                .map(GroupMenu::getMenu)
                .map(menuMapper::toDTO)
                .toList();

        log.info("Found {} menus for groupId: {}",
                menus.size(),
                groupId
        );

        return menus;
    }

    // =====================================================
    // REPLACE GROUP MENUS
    // =====================================================
    @Override
    @Transactional
    public List<GroupMenuDTO> replaceGroupMenus(
            UUID groupId,
            List<UUID> menuIds
    ) {
        UUID tenantId = TenantContextHolder.getTenant().getId();

        // =============================================
        // VALIDATE GROUP
        // =============================================
        Group group =
                groupRepository.findById(groupId)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found: " + groupId
                                )
                        );

        if (!Objects.equals(group.getTenantId(), tenantId)) {
            throw new RuntimeException("Group does not belong to current tenant: " + groupId);
        }

        // =============================================
        // DELETE EXISTING MENUS
        // =============================================
        groupMenuRepository.deleteByGroup_Id(groupId);

        // IMPORTANT
        groupMenuRepository.flush();

        // =============================================
        // EMPTY MENUS
        // =============================================
        if (menuIds == null || menuIds.isEmpty()) {

            return List.of();
        }

        // =============================================
        // LOAD MENUS
        // =============================================
        List<Menu> menus =
                menuRepository.findAllById(menuIds);

        if (menus.size() != menuIds.stream().distinct().count()) {
            throw new RuntimeException("One or more menus were not found");
        }

        menus.forEach(menu -> {
            if (!Objects.equals(menu.getTenantId(), tenantId)) {
                throw new RuntimeException("Menu does not belong to current tenant: " + menu.getId());
            }
        });

        // =============================================
        // CREATE NEW MAPPINGS
        // =============================================
        List<GroupMenu> entities =
                menus.stream()

                        .map(menu -> GroupMenu.builder()

                                .tenantId(group.getTenantId())

                                .group(group)

                                .menu(menu)

                                .build())

                        .collect(Collectors.toList());

        // =============================================
        // SAVE
        // =============================================
        List<GroupMenu> groupMenus =
                groupMenuRepository.saveAll(entities);

        return groupMenuMapper.toDTOList(groupMenus);
    }
}
