package com.codeshare.airline.tenant.controller.partner;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerAcceptanceRuleDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerAcceptanceRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant-partner-acceptance-rules")
@RequiredArgsConstructor
public class CodesharePartnerAcceptanceRuleController {

    private final CodesharePartnerAcceptanceRuleService service;

    @PostMapping
    public CodesharePartnerAcceptanceRuleDTO create(@RequestBody CodesharePartnerAcceptanceRuleDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public CodesharePartnerAcceptanceRuleDTO update(@PathVariable Long id, @RequestBody CodesharePartnerAcceptanceRuleDTO dto) { return service.update(id, dto); }

    @GetMapping("/{id}")
    public CodesharePartnerAcceptanceRuleDTO getById(@PathVariable Long id) { return service.getById(id); }

    @GetMapping
    public List<CodesharePartnerAcceptanceRuleDTO> getAll() { return service.getAll(); }

    @GetMapping("/internal/{tenantCode}/{partnerCode}")
    public CodesharePartnerAcceptanceRuleDTO resolve(
            @PathVariable String tenantCode,
            @PathVariable String partnerCode,
            @RequestParam MessageType messageType
    ) {
        return service.resolve(tenantCode, partnerCode, messageType);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) { service.delete(id); return CSMConstants.NO_DATA; }
}
