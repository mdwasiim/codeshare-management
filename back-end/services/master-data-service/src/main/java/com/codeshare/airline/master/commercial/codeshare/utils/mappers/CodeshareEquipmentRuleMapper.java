package com.codeshare.airline.master.commercial.codeshare.utils.mappers;

import com.codeshare.airline.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareEquipmentRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareEquipmentRuleMapper
        extends CSMGenericMapper<CodeshareEquipmentRule, CodeshareEquipmentRuleDTO> {

    @Mapping(source = "flightMapping.id", target = "flightMappingId")
    @Mapping(source = "aircraftType.id", target = "aircraftTypeId")
    CodeshareEquipmentRuleDTO toDTO(CodeshareEquipmentRule entity);
}