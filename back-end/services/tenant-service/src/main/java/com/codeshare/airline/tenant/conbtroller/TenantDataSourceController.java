package com.codeshare.airline.tenant.conbtroller;

import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantDataSourceDTO;
import com.codeshare.airline.tenant.service.TenantDataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenant-datasources")
@RequiredArgsConstructor
public class TenantDataSourceController {

    private final TenantDataSourceService service;

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody TenantDataSourceDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody TenantDataSourceDTO dto
    ) {
        return ResponseEntity.ok(ServiceResponse.success(service.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ServiceResponse> getAll() {
        return ResponseEntity.ok(ServiceResponse.success(service.getAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
