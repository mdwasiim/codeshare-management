package com.codeshare.airline.auth.controller.menu;

import com.codeshare.airline.auth.service.MenuRoleAssignmentService;
import com.codeshare.airline.common.auth.model.MenuRoleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-role")
@RequiredArgsConstructor
public class MenuRoleAssignmentController {

    private final MenuRoleAssignmentService service;

    @PostMapping("/{roleId}/{menuId}")
    public ResponseEntity<MenuRoleDTO> assign(
            @PathVariable UUID roleId,
            @PathVariable UUID menuId
    ) {
        return ResponseEntity.ok(service.assignMenuToRole(roleId, menuId));
    }

    @DeleteMapping("/{roleId}/{menuId}")
    public ResponseEntity<Void> remove(
            @PathVariable UUID roleId,
            @PathVariable UUID menuId
    ) {
        service.removeMenuFromRole(roleId, menuId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<MenuRoleDTO>> getByRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(service.getMenusByRole(roleId));
    }

    @GetMapping("/menu/{menuId}")
    public ResponseEntity<List<MenuRoleDTO>> getByMenu(@PathVariable UUID menuId) {
        return ResponseEntity.ok(service.getRolesByMenu(menuId));
    }
}
