package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareDayRuleDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareDayRuleService
        extends BaseService<CodeshareDayRuleDTO, UUID> {

    List<CodeshareDayRuleDTO> getByFlightMapping(UUID mappingId);
}