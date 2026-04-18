package com.codeshare.airline.master.commercial.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareAgreementService
        extends BaseService<CodeshareAgreementDTO, UUID> {

    List<CodeshareAgreementDTO> getByMarketingAirline(UUID airlineId);

    List<CodeshareAgreementDTO> getByOperatingAirline(UUID airlineId);
}