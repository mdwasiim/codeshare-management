package com.codeshare.airline.master.aircraft.utils.mappers;

import com.codeshare.airline.dto.aircraft.AircraftConfigurationDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.eitities.AircraftConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AircraftConfigurationMapper
        extends CSMGenericMapper<AircraftConfiguration, AircraftConfigurationDTO> {

    @Mapping(source = "aircraftType.id", target = "aircraftTypeId")
    @Mapping(source = "airline.id", target = "airlineId")
    AircraftConfigurationDTO toDTO(AircraftConfiguration entity);
}