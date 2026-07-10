package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;

import java.util.List;
import java.util.UUID;

public interface CodesharePartnerService {
    CodesharePartnerDTO create(CodesharePartnerDTO dto);
    CodesharePartnerDTO update(UUID id, CodesharePartnerDTO dto);
    CodesharePartnerDTO getById(UUID id);
    List<CodesharePartnerDTO> getAll();
    void delete(UUID id);
}
