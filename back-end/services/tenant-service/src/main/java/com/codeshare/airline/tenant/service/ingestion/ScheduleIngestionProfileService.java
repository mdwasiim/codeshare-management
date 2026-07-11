package com.codeshare.airline.tenant.service.ingestion;


import com.codeshare.airline.platform.core.dto.tenant.TenantIngestionProfileDTO;

import java.util.List;
import java.util.UUID;

public interface ScheduleIngestionProfileService {

    TenantIngestionProfileDTO create(TenantIngestionProfileDTO dto);

    TenantIngestionProfileDTO update(UUID id, TenantIngestionProfileDTO dto);

    TenantIngestionProfileDTO getByTenantCode(String tenantCode);

    List<TenantIngestionProfileDTO> getAll();

    void delete(UUID id);

    void enable(UUID id, boolean enabled);
}
