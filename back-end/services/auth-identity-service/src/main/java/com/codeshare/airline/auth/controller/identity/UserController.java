package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.UserService;
import com.codeshare.airline.common.auth.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<UserDTO>> getByTenant(@PathVariable UUID tenantId) {
        return ResponseEntity.ok(userService.getByTenant(tenantId));
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<UserDTO>> getByOrganization(@PathVariable UUID organizationId) {
        return ResponseEntity.ok(userService.getByOrganization(organizationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
