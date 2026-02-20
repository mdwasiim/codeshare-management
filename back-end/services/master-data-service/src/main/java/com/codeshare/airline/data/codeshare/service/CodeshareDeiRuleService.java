package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareDeiRuleDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareDeiRuleService
        extends BaseService<CodeshareDeiRuleDTO, UUID> {

    List<CodeshareDeiRuleDTO> getByFlightMapping(UUID flightMappingId);
}