package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant-partner-profiles")
@RequiredArgsConstructor
public class CodesharePartnerProfileController {

    private final CodesharePartnerProfileService service;

    @PostMapping
    public CodesharePartnerProfileDTO create(@RequestBody CodesharePartnerProfileDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerProfileDTO update(@PathVariable Long id, @RequestBody CodesharePartnerProfileDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerProfileDTO getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerProfileDTO> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
