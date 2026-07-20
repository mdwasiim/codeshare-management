package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;

import java.util.List;

public interface CodesharePartnerDistributionProfileService {
    CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO update(Long id, CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO getById(Long id);
    List<CodesharePartnerDistributionProfileDTO> getAll();
    List<CodesharePartnerDistributionProfileDTO> getCurrent(String tenantCode);
    List<CodesharePartnerDistributionProfileDTO> resolve(String tenantCode, String partnerCode, MessageType messageType);
    void delete(Long id);
}
