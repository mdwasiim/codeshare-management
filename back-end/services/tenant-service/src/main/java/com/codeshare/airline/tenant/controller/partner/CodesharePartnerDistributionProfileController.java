package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerDistributionProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenant-partner-distribution-profiles")
@RequiredArgsConstructor
public class CodesharePartnerDistributionProfileController {

    private final CodesharePartnerDistributionProfileService service;

    @PostMapping
    public CodesharePartnerDistributionProfileDTO create(@RequestBody CodesharePartnerDistributionProfileDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerDistributionProfileDTO update(@PathVariable UUID id, @RequestBody CodesharePartnerDistributionProfileDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerDistributionProfileDTO getById(@PathVariable UUID id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerDistributionProfileDTO> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) { service.delete(id); return CSMConstants.NO_DATA; }
}
