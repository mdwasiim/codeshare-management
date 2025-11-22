package com.codeshare.airline.auth.controller.menu;

import com.codeshare.airline.auth.service.MenuService;
import com.codeshare.airline.common.auth.model.MenuDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService service;

    @PostMapping
    public ResponseEntity<MenuDTO> create(@RequestBody MenuDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> update(@PathVariable UUID id, @RequestBody MenuDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/roots")
    public ResponseEntity<List<MenuDTO>> getRootMenus(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(service.getRootMenus(tenantId));
    }

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(service.getAllByTenant(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

