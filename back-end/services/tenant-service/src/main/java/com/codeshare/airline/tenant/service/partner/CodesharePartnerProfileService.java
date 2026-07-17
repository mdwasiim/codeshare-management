package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;

import java.util.List;

public interface CodesharePartnerProfileService {
    CodesharePartnerProfileDTO create(CodesharePartnerProfileDTO dto);
    CodesharePartnerProfileDTO update(Long id, CodesharePartnerProfileDTO dto);
    CodesharePartnerProfileDTO getById(Long id);
    List<CodesharePartnerProfileDTO> getAll();
    void delete(Long id);
}
