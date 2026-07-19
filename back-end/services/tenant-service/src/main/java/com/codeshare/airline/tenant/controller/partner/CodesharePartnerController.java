package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.tenant.common.ExactFilter;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenant-partners")
@RequiredArgsConstructor
public class CodesharePartnerController {

    private final CodesharePartnerService service;

    @PostMapping
    public CodesharePartnerDTO create(@RequestBody CodesharePartnerDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerDTO update(@PathVariable Long id, @RequestBody CodesharePartnerDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerDTO getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerDTO> getAll(@RequestParam Map<String, String> filters) { return ExactFilter.apply(service.getAll(), filters); }

    @GetMapping("/current")
    public List<CodesharePartnerDTO> getCurrent(@RequestHeader("X-Tenant-Id") String tenantCode) {
        return service.getCurrent(tenantCode);
    }

    @PostMapping("/current")
    public CodesharePartnerDTO createCurrent(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @RequestBody CodesharePartnerDTO dto
    ) {
        return service.createCurrent(tenantCode, dto);
    }

    @PutMapping("/current/{id}")
    public CodesharePartnerDTO updateCurrent(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @PathVariable Long id,
            @RequestBody CodesharePartnerDTO dto
    ) {
        return service.updateCurrent(tenantCode, id, dto);
    }

    @DeleteMapping("/current/{id}")
    public String deleteCurrent(@RequestHeader("X-Tenant-Id") String tenantCode, @PathVariable Long id) {
        service.deleteCurrent(tenantCode, id);
        return CSMConstants.NO_DATA;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
