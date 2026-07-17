package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;

import java.util.List;

public interface CodesharePartnerCommunicationProfileService {
    CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO update(Long id, CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO getById(Long id);
    List<CodesharePartnerCommunicationProfileDTO> getAll();
    void delete(Long id);
}
