package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerCommunicationProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<CodesharePartnerCommunicationProfileDTO> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
