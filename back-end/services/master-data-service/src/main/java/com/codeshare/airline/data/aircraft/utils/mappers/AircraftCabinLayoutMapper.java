package com.codeshare.airline.data.aircraft.utils.mappers;

import com.codeshare.airline.core.dto.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.aircraft.eitities.AircraftCabinLayout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AircraftCabinLayoutMapper
        extends CSMGenericMapper<AircraftCabinLayout, AircraftCabinLayoutDTO> {

    @Mapping(source = "aircraftConfiguration.id", target = "aircraftConfigurationId")
    AircraftCabinLayoutDTO toDTO(AircraftCabinLayout entity);
}