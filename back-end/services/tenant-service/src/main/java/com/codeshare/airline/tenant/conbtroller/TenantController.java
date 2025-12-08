package com.codeshare.airline.tenant.conbtroller;

import com.codeshare.airline.common.services.constant.AppConstan;
import com.codeshare.airline.common.services.response.ServiceResponse;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody TenantDTO dto) {
        return ResponseEntity.ok(ServiceResponse.success(tenantService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable UUID id,
            @RequestBody TenantDTO dto
    ) {
        return ResponseEntity.ok(ServiceResponse.success(tenantService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ServiceResponse.success(tenantService.getById(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ServiceResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(ServiceResponse.success(tenantService.getByCode(code)));
    }

    @GetMapping
    public ResponseEntity<ServiceResponse> getAllTenants() {
        return ResponseEntity.ok(ServiceResponse.success(tenantService.getAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ServiceResponse> delete(@PathVariable UUID id) {
        tenantService.delete(id);
        return ResponseEntity.ok(ServiceResponse.success(AppConstan.NO_DATA));
    }
}
