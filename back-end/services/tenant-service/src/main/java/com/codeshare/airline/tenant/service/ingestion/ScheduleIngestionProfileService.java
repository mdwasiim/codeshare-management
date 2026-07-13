package com.codeshare.airline.tenant.service.ingestion;


import com.codeshare.airline.platform.core.dto.tenant.TenantIngestionProfileDTO;

import java.util.List;

public interface ScheduleIngestionProfileService {

    TenantIngestionProfileDTO create(TenantIngestionProfileDTO dto);

    TenantIngestionProfileDTO update(Long id, TenantIngestionProfileDTO dto);

    TenantIngestionProfileDTO getByTenantCode(String tenantCode);

    List<TenantIngestionProfileDTO> getAll();

    void delete(Long id);

    void enable(Long id, boolean enabled);
}
