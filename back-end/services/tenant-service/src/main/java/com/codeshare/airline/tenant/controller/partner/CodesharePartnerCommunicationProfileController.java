package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.tenant.common.ExactFilter;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerCommunicationProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenant-partner-communication-profiles")
@RequiredArgsConstructor
public class CodesharePartnerCommunicationProfileController {

    private final CodesharePartnerCommunicationProfileService service;

    @PostMapping
    public CodesharePartnerCommunicationProfileDTO create(@RequestBody CodesharePartnerCommunicationProfileDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerCommunicationProfileDTO update(@PathVariable Long id, @RequestBody CodesharePartnerCommunicationProfileDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerCommunicationProfileDTO getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerCommunicationProfileDTO> getAll(@RequestParam Map<String, String> filters) { return ExactFilter.apply(service.getAll(), filters); }

    @GetMapping("/current")
    public List<CodesharePartnerCommunicationProfileDTO> getCurrent(@RequestHeader("X-Tenant-Id") String tenantCode) {
        return service.getCurrent(tenantCode);
    }

    @GetMapping("/internal/{tenantCode}/{partnerCode}")
    public List<CodesharePartnerCommunicationProfileDTO> resolve(
            @PathVariable String tenantCode,
            @PathVariable String partnerCode,
            @RequestParam CommunicationProtocol protocol
    ) {
        return service.resolve(tenantCode, partnerCode, protocol);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
