package com.codeshare.airline.auth.controller.identity;

import com.codeshare.airline.auth.service.GroupService;
import com.codeshare.airline.common.auth.model.GroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupDTO> create(@RequestBody GroupDTO dto) {
        return ResponseEntity.ok(groupService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> update(@PathVariable UUID id, @RequestBody GroupDTO dto) {
        return ResponseEntity.ok(groupService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getByTenant(@RequestParam UUID tenantId) {
        return ResponseEntity.ok(groupService.getByTenant(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
