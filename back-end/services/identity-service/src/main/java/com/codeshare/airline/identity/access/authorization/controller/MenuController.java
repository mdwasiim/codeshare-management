package com.codeshare.airline.identity.access.authorization.controller;

import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.authorization.service.MenuService;
import com.codeshare.airline.web.response.CSMResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CSMResponse
@RestController
@RequestMapping("/menus")
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

    @GetMapping("/manage/all")
    public List<MenuDTO> getAllForManagement() {
        return service.getAllForManagement();
    }

    // -----------------------------------------------------------
    // DELETE MENU (Soft delete recommended)
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    // -----------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping
    public List<MenuDTO> getAll(  @RequestParam(required = false) Boolean rootOnly) {
        if (Boolean.TRUE.equals(rootOnly)) {
            return service.getRootMenus();
        }

        return service.getAllByTenant();
    }
}
