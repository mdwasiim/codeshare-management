package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.rbac.Role;
import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.auth.utils.mappers.MenuRoleMapper;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.repository.MenuRoleRepository;
import com.codeshare.airline.auth.repository.RoleRepository;
import com.codeshare.airline.auth.service.MenuRoleAssignmentService;
import com.codeshare.airline.common.auth.identity.model.MenuRoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuRoleAssignmentServiceImpl implements MenuRoleAssignmentService {

    private final RoleRepository roleRepo;
    private final MenuRepository menuRepo;
    private final MenuRoleRepository repo;
    private final MenuRoleMapper mapper;

    @Autowired
    public MenuRoleAssignmentServiceImpl(RoleRepository roleRepo, MenuRepository menuRepo, MenuRoleRepository repo, MenuRoleMapper mapper) {
        this.roleRepo = roleRepo;
        this.menuRepo = menuRepo;
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public MenuRoleDTO assignMenuToRole(UUID roleId, UUID menuId) {

        if (repo.existsByRoleIdAndMenuId(roleId, menuId))
            throw new RuntimeException("Menu already assigned to role.");

        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Menu menu = menuRepo.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        MenuRole mr = MenuRole.builder()
                .role(role)
                .menu(menu)
                .build();

        return mapper.toDTO(repo.save(mr));
    }

    @Override
    public void removeMenuFromRole(UUID roleId, UUID menuId) {
        repo.findByRoleId(roleId).stream()
                .filter(x -> x.getMenu().getId().equals(menuId))
                .findFirst()
                .ifPresent(repo::delete);
    }

    @Override
    public List<MenuRoleDTO> getMenusByRole(UUID roleId) {
        return mapper.toDTOList(repo.findByRoleId(roleId));
    }

    @Override
    public List<MenuRoleDTO> getRolesByMenu(UUID menuId) {
        return mapper.toDTOList(repo.findByMenuId(menuId));
    }
}
