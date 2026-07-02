package com.codeshare.airline.master.flightcommercial.schedule.mappers;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TrafficRestrictionQualifierDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TrafficRestrictionQualifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface TrafficRestrictionQualifierMapper extends CSMGenericMapper<TrafficRestrictionQualifier, TrafficRestrictionQualifierDTO> {
    @Mapping(source = "trafficRestrictionCode.id", target = "trafficRestrictionCodeId")
    TrafficRestrictionQualifierDTO toDTO(TrafficRestrictionQualifier entity);

    @Mapping(target = "trafficRestrictionCode", ignore = true)
    TrafficRestrictionQualifier toEntity(TrafficRestrictionQualifierDTO dto);

    @Mapping(target = "trafficRestrictionCode", ignore = true)
    void updateEntityFromDto(TrafficRestrictionQualifierDTO dto, @MappingTarget TrafficRestrictionQualifier entity);
}
