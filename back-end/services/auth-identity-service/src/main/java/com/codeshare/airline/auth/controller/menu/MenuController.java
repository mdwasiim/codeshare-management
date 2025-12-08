package com.codeshare.airline.auth.controller.menu;

import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.common.auth.identity.model.MenuDTO;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService service;

    // -----------------------------------------------------------
    // CREATE MENU
    // -----------------------------------------------------------
    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody MenuDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.create(dto)));
    }

    // -----------------------------------------------------------
    // UPDATE MENU
    // -----------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody MenuDTO dto
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.update(id, dto)));
    }

    // -----------------------------------------------------------
    // GET MENU BY ID
    // -----------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(service.getById(id)));
    }

    // -----------------------------------------------------------
    // GET ROOT MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping("/roots")
    public ResponseEntity<ServiceResponse> getRootMenus(
            @RequestParam UUID tenantId
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.getRootMenus(tenantId)));
    }

    // -----------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping
    public ResponseEntity<ServiceResponse> getAllByTenant(
            @RequestParam UUID tenantId
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.getAllByTenant(tenantId)));
    }

    // -----------------------------------------------------------
    // DELETE MENU (Soft delete recommended)
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
