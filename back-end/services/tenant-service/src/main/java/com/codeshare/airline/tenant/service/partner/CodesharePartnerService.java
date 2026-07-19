package com.codeshare.airline.tenant.service.partner;

import com.codeshare.airline.platform.core.dto.master.airline.CodesharePartnerDTO;

import java.util.List;

public interface CodesharePartnerService {
    CodesharePartnerDTO create(CodesharePartnerDTO dto);
    CodesharePartnerDTO update(Long id, CodesharePartnerDTO dto);
    CodesharePartnerDTO getById(Long id);
    List<CodesharePartnerDTO> getAll();
    List<CodesharePartnerDTO> getCurrent(String tenantCode);
    CodesharePartnerDTO createCurrent(String tenantCode, CodesharePartnerDTO dto);
    CodesharePartnerDTO updateCurrent(String tenantCode, Long id, CodesharePartnerDTO dto);
    void deleteCurrent(String tenantCode, Long id);
    void delete(Long id);
}
