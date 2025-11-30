package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.common.jpa.BaseRepository;

import java.util.List;
import java.util.UUID;

public interface MenuRoleRepository extends BaseRepository<MenuRole, UUID> {

    List<MenuRole> findByRoleId(UUID roleId);

    List<MenuRole> findByMenuId(UUID menuId);

    boolean existsByRoleIdAndMenuId(UUID roleId, UUID menuId);
}

