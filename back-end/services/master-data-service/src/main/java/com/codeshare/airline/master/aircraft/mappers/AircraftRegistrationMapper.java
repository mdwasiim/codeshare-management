package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.master.aircraft.AircraftRegistrationDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AircraftRegistrationMapper
        extends CSMGenericMapper<AircraftRegistration, AircraftRegistrationDTO> {

    @Mapping(source = "aircraftType.id", target = "aircraftTypeId")
    @Mapping(source = "aircraftConfiguration.id", target = "aircraftConfigurationId")
    @Mapping(source = "aircraftOwner.id", target = "aircraftOwnerId")
    @Mapping(source = "operatorAirline.id", target = "operatorAirlineId")
    AircraftRegistrationDTO toDTO(AircraftRegistration entity);

    @Mapping(target = "aircraftType", ignore = true)
    @Mapping(target = "aircraftConfiguration", ignore = true)
    @Mapping(target = "aircraftOwner", ignore = true)
    @Mapping(target = "operatorAirline", ignore = true)
    AircraftRegistration toEntity(AircraftRegistrationDTO dto);

    @Mapping(target = "aircraftType", ignore = true)
    @Mapping(target = "aircraftConfiguration", ignore = true)
    @Mapping(target = "aircraftOwner", ignore = true)
    @Mapping(target = "operatorAirline", ignore = true)
    void updateEntityFromDto(AircraftRegistrationDTO dto, @MappingTarget AircraftRegistration entity);
}
