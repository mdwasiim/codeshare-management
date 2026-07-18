package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.tenant.common.ExactFilter;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerDistributionProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenant-partner-distribution-profiles")
@RequiredArgsConstructor
public class CodesharePartnerDistributionProfileController {

    private final CodesharePartnerDistributionProfileService service;

    @PostMapping
    public CodesharePartnerDistributionProfileDTO create(@RequestBody CodesharePartnerDistributionProfileDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerDistributionProfileDTO update(@PathVariable Long id, @RequestBody CodesharePartnerDistributionProfileDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerDistributionProfileDTO getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerDistributionProfileDTO> getAll(@RequestParam Map<String, String> filters) { return ExactFilter.apply(service.getAll(), filters); }

    @GetMapping("/internal/{tenantCode}/{partnerCode}")
    public List<CodesharePartnerDistributionProfileDTO> resolve(
            @PathVariable String tenantCode,
            @PathVariable String partnerCode,
            @RequestParam MessageType messageType
    ) {
        return service.resolve(tenantCode, partnerCode, messageType);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
