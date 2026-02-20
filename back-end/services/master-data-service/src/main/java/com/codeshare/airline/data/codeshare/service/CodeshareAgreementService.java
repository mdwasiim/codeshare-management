package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareAgreementService
        extends BaseService<CodeshareAgreementDTO, UUID> {

    List<CodeshareAgreementDTO> getByMarketingAirline(UUID airlineId);

    List<CodeshareAgreementDTO> getByOperatingAirline(UUID airlineId);
}