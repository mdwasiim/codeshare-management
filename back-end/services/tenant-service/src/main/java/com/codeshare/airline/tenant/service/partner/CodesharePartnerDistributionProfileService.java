package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;

import java.util.List;
import java.util.UUID;

public interface CodesharePartnerDistributionProfileService {
    CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO update(UUID id, CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO getById(UUID id);
    List<CodesharePartnerDistributionProfileDTO> getAll();
    void delete(UUID id);
}
