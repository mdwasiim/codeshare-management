package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;

import java.util.List;

public interface CodesharePartnerDistributionProfileService {
    CodesharePartnerDistributionProfileDTO create(CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO update(Long id, CodesharePartnerDistributionProfileDTO dto);
    CodesharePartnerDistributionProfileDTO getById(Long id);
    List<CodesharePartnerDistributionProfileDTO> getAll();
    void delete(Long id);
}
