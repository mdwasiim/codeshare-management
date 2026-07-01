package com.codeshare.airline.master.codeshare.service;

import com.codeshare.airline.core.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface CodeshareEquipmentRuleService
        extends BaseService<CodeshareEquipmentRuleDTO, UUID> {

    List<CodeshareEquipmentRuleDTO> getByFlightMapping(UUID flightMappingId);
}