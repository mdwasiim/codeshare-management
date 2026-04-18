package com.codeshare.airline.master.aircraft.utils.mappers;

import com.codeshare.airline.dto.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.eitities.AircraftCabinLayout;
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