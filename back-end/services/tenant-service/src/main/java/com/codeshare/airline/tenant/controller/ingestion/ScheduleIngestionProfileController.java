package com.codeshare.airline.tenant.controller.ingestion;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.tenant.TenantIngestionProfileDTO;
import com.codeshare.airline.tenant.common.ExactFilter;
import com.codeshare.airline.tenant.service.ingestion.ScheduleIngestionProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenant-ingestion-profiles")
@RequiredArgsConstructor
public class ScheduleIngestionProfileController {

    private final ScheduleIngestionProfileService service;

    @PostMapping
    public TenantIngestionProfileDTO create(@RequestBody TenantIngestionProfileDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TenantIngestionProfileDTO update(@PathVariable Long id, @RequestBody TenantIngestionProfileDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping("/tenant/{tenantCode}")
    public TenantIngestionProfileDTO getByTenantCode(@PathVariable String tenantCode) {
        return service.getByTenantCode(tenantCode);
    }

    @GetMapping
    public List<TenantIngestionProfileDTO> getAll(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(service.getAll(), filters);
    }

    @GetMapping("/internal/all")
    public List<TenantIngestionProfileDTO> getAllInternal(@RequestParam Map<String, String> filters) {
        return ExactFilter.apply(service.getAll(), filters);
    }

    @PatchMapping("/{id}/enabled")
    public String enable(@PathVariable Long id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
        return CSMConstants.NO_DATA;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return CSMConstants.NO_DATA;
    }
}
