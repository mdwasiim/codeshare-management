package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.master.aircraft.AirlineFleetDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AirlineFleetProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AirlineFleetMapper
        extends CSMGenericMapper<AirlineFleetProfile, AirlineFleetDTO> {

    @Mapping(source = "airline.id", target = "airlineId")
    @Mapping(source = "aircraftConfiguration.id", target = "aircraftConfigurationId")
    AirlineFleetDTO toDTO(AirlineFleetProfile entity);
}