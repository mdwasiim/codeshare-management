package com.codeshare.airline.data.aircraft.utils.mappers;

import com.codeshare.airline.core.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.aircraft.eitities.AirlineFleet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AirlineFleetMapper
        extends CSMGenericMapper<AirlineFleet, AirlineFleetDTO> {

    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "aircraftConfiguration.id", target = "aircraftConfigurationId")
    AirlineFleetDTO toDTO(AirlineFleet entity);
}