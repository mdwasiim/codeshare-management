package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareRouteDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareRouteService
        extends BaseService<CodeshareRouteDTO, UUID> {

    List<CodeshareRouteDTO> getByAgreement(UUID agreementId);
}