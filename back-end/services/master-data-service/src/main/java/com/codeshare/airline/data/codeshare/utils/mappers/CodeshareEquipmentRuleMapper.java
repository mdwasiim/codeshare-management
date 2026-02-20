package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareEquipmentRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareEquipmentRuleMapper
        extends CSMGenericMapper<CodeshareEquipmentRule, CodeshareEquipmentRuleDTO> {

    @Mapping(source = "flightMapping.id", target = "flightMappingId")
    @Mapping(source = "aircraftType.id", target = "aircraftTypeId")
    CodeshareEquipmentRuleDTO toDTO(CodeshareEquipmentRule entity);
}