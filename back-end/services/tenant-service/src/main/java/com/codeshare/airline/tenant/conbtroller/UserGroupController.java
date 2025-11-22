package com.codeshare.airline.tenant.conbtroller;


import com.codeshare.airline.common.tenant.model.UserGroupDTO;
import com.codeshare.airline.tenant.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupService userGroupService;

    @PostMapping
    public ResponseEntity<UserGroupDTO> create(@RequestBody UserGroupDTO dto) {
        return ResponseEntity.ok(userGroupService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGroupDTO> update(
            @PathVariable UUID id,
            @RequestBody UserGroupDTO dto) {
        return ResponseEntity.ok(userGroupService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userGroupService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserGroupDTO>> getAll(
            @RequestParam UUID tenantId) {
        return ResponseEntity.ok(userGroupService.getAll(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

