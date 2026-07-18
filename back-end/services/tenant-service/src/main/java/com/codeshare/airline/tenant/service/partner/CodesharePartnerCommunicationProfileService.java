package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;

import java.util.List;

public interface CodesharePartnerCommunicationProfileService {
    CodesharePartnerCommunicationProfileDTO create(CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO update(Long id, CodesharePartnerCommunicationProfileDTO dto);
    CodesharePartnerCommunicationProfileDTO getById(Long id);
    List<CodesharePartnerCommunicationProfileDTO> getAll();
    List<CodesharePartnerCommunicationProfileDTO> resolve(String tenantCode, String partnerCode, CommunicationProtocol protocol);
    void delete(Long id);
}
