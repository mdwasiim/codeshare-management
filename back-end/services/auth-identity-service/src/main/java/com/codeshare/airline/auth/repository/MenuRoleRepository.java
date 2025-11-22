package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.menu.MenuRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuRoleRepository extends JpaRepository<MenuRole, UUID> {

    List<MenuRole> findByRoleId(UUID roleId);

    List<MenuRole> findByMenuId(UUID menuId);

    boolean existsByRoleIdAndMenuId(UUID roleId, UUID menuId);
}

