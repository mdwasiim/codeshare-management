package com.codeshare.airline.master.commercial.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareDeiRuleDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareDeiRuleService
        extends BaseService<CodeshareDeiRuleDTO, UUID> {

    List<CodeshareDeiRuleDTO> getByFlightMapping(UUID flightMappingId);
}