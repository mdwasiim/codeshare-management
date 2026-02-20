package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareFlightMappingService
        extends BaseService<CodeshareFlightMappingDTO, UUID> {

    List<CodeshareFlightMappingDTO> getByAgreement(UUID agreementId);
}