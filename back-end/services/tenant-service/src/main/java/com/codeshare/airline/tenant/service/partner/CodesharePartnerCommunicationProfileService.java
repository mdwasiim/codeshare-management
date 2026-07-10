package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;

import java.util.List;
import java.util.UUID;

public interface CodesharePartnerCommunicationProfileService {
    CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO update(UUID id, CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO getById(UUID id);
    List<CodesharePartnerCommunicationProfileDTO> getAll();
    void delete(UUID id);
}
