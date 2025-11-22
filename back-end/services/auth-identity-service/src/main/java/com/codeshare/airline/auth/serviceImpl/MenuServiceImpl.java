package com.codeshare.airline.auth.serviceImpl;

import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.auth.utils.mappers.MenuMapper;
import com.codeshare.airline.auth.repository.MenuRepository;
import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.common.auth.model.MenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;
    private final MenuMapper mapper;

    @Autowired
    public MenuServiceImpl(MenuRepository repository, MenuMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MenuDTO create(MenuDTO dto) {

        if (dto.getTenantId() == null)
            throw new IllegalArgumentException("tenantId is required");

        if (dto.getName() == null || dto.getName().isBlank())
            throw new IllegalArgumentException("name is required");

        if (repository.existsByNameAndTenantId(dto.getName(), dto.getTenantId()))
            throw new RuntimeException("Menu already exists for tenant");

        Menu entity = mapper.toEntity(dto);

        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        }

        Menu saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public MenuDTO update(UUID id, MenuDTO dto) {

        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getUrl() != null) entity.setUrl(dto.getUrl());
        if (dto.getIcon() != null) entity.setIcon(dto.getIcon());
        if (dto.getBadge() != null) entity.setBadge(dto.getBadge());
        if (dto.getIconComponent() != null) entity.setIconComponent(dto.getIconComponent());
        entity.setTitle(dto.getTitle());

        if (dto.getParentId() != null) {
            Menu parent = repository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent menu not found"));
            entity.setParentMenu(parent);
        }

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public MenuDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getRootMenus(UUID tenantId) {
        List<Menu> menus = repository.findByTenantIdAndParentMenuIsNull(tenantId);
        return mapper.toDTOList(menus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDTO> getAllByTenant(UUID tenantId) {
        return mapper.toDTOList(repository.findByTenantId(tenantId));
    }

    @Override
    public void delete(UUID id) {
        Menu entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found: " + id));
        repository.delete(entity);
    }
}
