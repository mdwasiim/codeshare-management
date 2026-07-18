package com.codeshare.airline.identity.access.authorization.controller;

import com.codeshare.airline.identity.access.common.ExactFilter;
import com.codeshare.airline.platform.core.dto.tenant.MenuDTO;
import com.codeshare.airline.identity.access.authorization.service.MenuService;
import com.codeshare.airline.platform.web.response.CSMResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public MenuDTO update(@PathVariable Long id,@RequestBody MenuDTO dto) {
        return service.update(id, dto);
    }

    // -----------------------------------------------------------
    // GET MENU BY ID
    // -----------------------------------------------------------
    @GetMapping("/{id}")
    public MenuDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/manage/all")
    public List<MenuDTO> getAllForManagement(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(service.getAllForManagement(), filters);
    }

    // -----------------------------------------------------------
    // DELETE MENU (Soft delete recommended)
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // -----------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping
    public List<MenuDTO> getAll(
            @RequestParam(required = false) Boolean rootOnly,
            @RequestParam Map<String, String> filters
    ) {
        if (Boolean.TRUE.equals(rootOnly)) {
            return ExactFilter.apply(service.getRootMenus(), filters);
        }

        return ExactFilter.apply(service.getAllByTenant(), filters);
    }
}
