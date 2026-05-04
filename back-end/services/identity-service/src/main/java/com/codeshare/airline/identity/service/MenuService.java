package com.codeshare.airline.identity.service;

import com.codeshare.airline.core.dto.tenant.MenuDTO;

import java.util.List;
import java.util.UUID;

public interface MenuService {

    MenuDTO create(MenuDTO dto);

    MenuDTO update(UUID id, MenuDTO dto);

    MenuDTO getById(UUID id);

    List<MenuDTO> getRootMenus();

    List<MenuDTO> getAllByTenant();

    void delete(UUID id);
}

