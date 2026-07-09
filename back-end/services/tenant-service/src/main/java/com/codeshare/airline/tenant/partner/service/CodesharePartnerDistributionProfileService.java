package com.codeshare.airline.tenant.partner.service;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;

import java.util.List;
import java.util.UUID;

public interface CodesharePartnerDistributionProfileService {
    CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO update(UUID id, CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO getById(UUID id);
    List<CodesharePartnerDistributionProfileDTO> getAll();
    void delete(UUID id);
}
