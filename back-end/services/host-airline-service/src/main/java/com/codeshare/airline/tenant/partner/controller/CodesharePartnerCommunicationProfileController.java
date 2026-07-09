package com.codeshare.airline.tenant.partner.controller;

import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.tenant.partner.service.CodesharePartnerCommunicationProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenant-partner-communication-profiles")
@RequiredArgsConstructor
public class CodesharePartnerCommunicationProfileController {

    private final CodesharePartnerCommunicationProfileService service;

    @PostMapping
    public CodesharePartnerCommunicationProfileDTO create(@RequestBody CodesharePartnerCommunicationProfileDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerCommunicationProfileDTO update(@PathVariable UUID id, @RequestBody CodesharePartnerCommunicationProfileDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerCommunicationProfileDTO getById(@PathVariable UUID id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerCommunicationProfileDTO> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) { service.delete(id); return CSMConstants.NO_DATA; }
}
