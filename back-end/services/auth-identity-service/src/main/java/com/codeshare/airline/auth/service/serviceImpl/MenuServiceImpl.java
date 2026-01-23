package com.codeshare.airline.auth.service.serviceImpl;

import com.codeshare.airline.auth.model.entities.Menu;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.auth.utils.mappers.MenuMapper;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;
    private final MenuMapper mapper;

    // ---------------------------------------------------------
    // CREATE NEW MENU FOR TENANT
    // ---------------------------------------------------------
    @Override
    public MenuDTO create(MenuDTO dto) {

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        if (dto.getName() == null || dto.getName().isBlank())
            throw new IllegalArgumentException("Menu name is required");

        // Check duplicate name in same tenant
        if (repository.existsByNameAndTenantId(dto.getName(), dto.getTenantId()))
            throw new RuntimeException("Menu already exists for tenant");

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

        // Update fields only if provided
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getUrl() != null) entity.setUrl(dto.getUrl());
        if (dto.getIcon() != null) entity.setIcon(dto.getIcon());
        if (dto.getBadge() != null) entity.setBadge(dto.getBadge());
        if (dto.getIconComponent() != null) entity.setIconComponent(dto.getIconComponent());
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());

        // Assign parent menu
        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
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
    public List<MenuDTO> getAllByTenant(UUID tenantId) {
        return mapper.toDTOList(repository.findByTenantId(tenantId));
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
