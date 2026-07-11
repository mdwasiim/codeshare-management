package com.codeshare.airline.identity.access.authorization.service;

import com.codeshare.airline.platform.core.dto.tenant.MenuBackupDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;

import java.util.List;
import java.util.UUID;

public interface MenuService {

    MenuDTO create(MenuDTO dto);

    MenuDTO update(UUID id, MenuDTO dto);

    MenuDTO getById(UUID id);

    List<MenuDTO> getRootMenus();

    List<MenuDTO> getAllByTenant();

    List<MenuDTO> getAllForManagement();

    void delete(UUID id);

    List<MenuBackupDTO> getAllMenuBackup();
}

