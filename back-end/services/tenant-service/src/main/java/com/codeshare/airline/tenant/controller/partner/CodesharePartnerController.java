package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<CodesharePartnerDTO> getAll() { return service.getAll(); }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
