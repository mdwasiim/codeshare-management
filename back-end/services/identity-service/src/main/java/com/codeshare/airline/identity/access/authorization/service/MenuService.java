package com.codeshare.airline.identity.access.authorization.service;

import com.codeshare.airline.platform.core.dto.tenant.MenuBackupDTO;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;

import java.util.List;

public interface MenuService {

    MenuDTO create(MenuDTO dto);

    MenuDTO update(Long id, MenuDTO dto);

    MenuDTO getById(Long id);

    List<MenuDTO> getRootMenus();

    List<MenuDTO> getAllByTenant();

    List<MenuDTO> getAllForManagement();

    void delete(Long id);

    List<MenuBackupDTO> getAllMenuBackup();
}

