package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.domain.TenantContextHolder;
import com.codeshare.airline.auth.common.CSMResponse;
import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CSMResponse
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController  {

    private final MenuService service;

    // -----------------------------------------------------------
    // CREATE MENU
    // -----------------------------------------------------------
    @PostMapping
    public MenuDTO create(@RequestBody MenuDTO dto) {
        return service.create(dto);
    }
    // -----------------------------------------------------------
    // UPDATE MENU
    // -----------------------------------------------------------
    @PutMapping("/{id}")
    public MenuDTO update(@PathVariable UUID id,@RequestBody MenuDTO dto) {
        return service.update(id, dto);
    }

    // -----------------------------------------------------------
    // GET MENU BY ID
    // -----------------------------------------------------------
    @GetMapping("/{id}")
    public MenuDTO getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    // -----------------------------------------------------------
    // GET ROOT MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping("/roots")
    public List<MenuDTO> getRootMenus(
            @RequestParam UUID tenantId
    ) {
        return service.getRootMenus(tenantId);
    }

    // -----------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping
    public List<MenuDTO> getAllMenuByTenant() {
        TenantContext tenant = TenantContextHolder.getTenant();
        return service.getAllByTenant(tenant.getTenantCode());

    }


    // -----------------------------------------------------------
    // DELETE MENU (Soft delete recommended)
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
