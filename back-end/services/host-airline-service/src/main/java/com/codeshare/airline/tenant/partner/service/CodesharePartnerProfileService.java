package com.codeshare.airline.tenant.partner.service;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;

import java.util.List;
import java.util.UUID;

public interface CodesharePartnerProfileService {
    CodesharePartnerProfileDTO create(CodesharePartnerProfileDTO dto);
    CodesharePartnerProfileDTO update(UUID id, CodesharePartnerProfileDTO dto);
    CodesharePartnerProfileDTO getById(UUID id);
    List<CodesharePartnerProfileDTO> getAll();
    void delete(UUID id);
}
