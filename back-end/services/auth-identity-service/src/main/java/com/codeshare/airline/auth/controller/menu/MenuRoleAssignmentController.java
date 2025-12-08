package com.codeshare.airline.auth.controller.menu;

import com.codeshare.airline.auth.service.MenuRoleAssignmentService;
import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/menu-role")
@RequiredArgsConstructor
public class MenuRoleAssignmentController {

    private final MenuRoleAssignmentService service;

    // ---------------------------------------------------------------------
    // ASSIGN MENU TO ROLE
    // ---------------------------------------------------------------------
    @PostMapping("/{roleId}/{menuId}")
    public ResponseEntity<ServiceResponse> assign(
            @PathVariable UUID roleId,
            @PathVariable UUID menuId
    ) {
        return ResponseEntity.ok(
                ServiceResponse.success(service.assignMenuToRole(roleId, menuId))
        );
    }

    // ---------------------------------------------------------------------
    // REMOVE MENU FROM ROLE
    // ---------------------------------------------------------------------
    @DeleteMapping("/{roleId}/{menuId}")
    public ResponseEntity<ServiceResponse> remove(
            @PathVariable UUID roleId,
            @PathVariable UUID menuId
    ) {
        service.removeMenuFromRole(roleId, menuId);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }

    // ---------------------------------------------------------------------
    // GET MENUS ASSIGNED TO A ROLE
    // ---------------------------------------------------------------------
    @GetMapping("/role/{roleId}")
    public ResponseEntity<ServiceResponse> getMenusByRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(
                ServiceResponse.success(service.getMenusByRole(roleId))
        );
    }

    // ---------------------------------------------------------------------
    // GET ROLES THAT HAVE ACCESS TO A MENU
    // ---------------------------------------------------------------------
    @GetMapping("/menu/{menuId}")
    public ResponseEntity<ServiceResponse> getRolesByMenu(@PathVariable UUID menuId) {
        return ResponseEntity.ok(
                ServiceResponse.success(service.getRolesByMenu(menuId))
        );
    }
}
