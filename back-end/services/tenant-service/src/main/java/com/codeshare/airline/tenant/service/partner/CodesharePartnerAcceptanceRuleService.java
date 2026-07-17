package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerAcceptanceRuleDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;

import java.util.List;

public interface CodesharePartnerAcceptanceRuleService {
    CodesharePartnerAcceptanceRuleDTO create(CodesharePartnerAcceptanceRuleDTO dto);
    CodesharePartnerAcceptanceRuleDTO update(Long id, CodesharePartnerAcceptanceRuleDTO dto);
    CodesharePartnerAcceptanceRuleDTO getById(Long id);
    List<CodesharePartnerAcceptanceRuleDTO> getAll();
    CodesharePartnerAcceptanceRuleDTO resolve(String tenantCode, String partnerCode, MessageType messageType);
    void delete(Long id);
}
