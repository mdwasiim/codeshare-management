package com.codeshare.airline.master.commercial.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareFlightMappingService
        extends BaseService<CodeshareFlightMappingDTO, UUID> {

    List<CodeshareFlightMappingDTO> getByAgreement(UUID agreementId);
}