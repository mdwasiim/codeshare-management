package com.codeshare.airline.auth.controller;

import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.domain.TenantContextHolder;
import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.core.response.CSMServiceResponse;
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
    public ResponseEntity<CSMServiceResponse> create(@RequestBody MenuDTO dto) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.create(dto)));
    }

    // -----------------------------------------------------------
    // UPDATE MENU
    // -----------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody MenuDTO dto
    ) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.update(id, dto)));
    }

    // -----------------------------------------------------------
    // GET MENU BY ID
    // -----------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getById(id)));
    }

    // -----------------------------------------------------------
    // GET ROOT MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping("/roots")
    public ResponseEntity<CSMServiceResponse> getRootMenus(
            @RequestParam UUID tenantId
    ) {
        return ResponseEntity.ok(CSMServiceResponse.success(service.getRootMenus(tenantId)));
    }

    // -----------------------------------------------------------
    // GET ALL MENUS FOR TENANT
    // -----------------------------------------------------------
    @GetMapping
    public ResponseEntity<CSMServiceResponse> getAllMenuByTenant() {
        TenantContext tenant = TenantContextHolder.getTenant();
        return ResponseEntity.ok(
                CSMServiceResponse.success(
                        service.getAllByTenant(tenant.getTenantCode())
                )
        );
    }


    // -----------------------------------------------------------
    // DELETE MENU (Soft delete recommended)
    // -----------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<CSMServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(CSMServiceResponse.success(CSMConstants.NO_DATA));
    }
}
