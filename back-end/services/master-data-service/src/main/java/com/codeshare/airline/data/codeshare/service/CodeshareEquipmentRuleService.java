package com.codeshare.airline.data.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareEquipmentRuleService
        extends BaseService<CodeshareEquipmentRuleDTO, UUID> {

    List<CodeshareEquipmentRuleDTO> getByFlightMapping(UUID flightMappingId);
}