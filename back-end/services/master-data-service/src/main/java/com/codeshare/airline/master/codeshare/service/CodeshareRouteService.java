package com.codeshare.airline.master.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareRouteDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareRouteService
        extends BaseService<CodeshareRouteDTO, UUID> {

    List<CodeshareRouteDTO> getByAgreement(UUID agreementId);
}