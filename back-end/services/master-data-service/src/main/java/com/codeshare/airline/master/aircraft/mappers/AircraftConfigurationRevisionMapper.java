package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.aircraft.AircraftConfigurationRevisionDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftConfigurationRevision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AircraftConfigurationRevisionMapper
        extends CSMGenericMapper<AircraftConfigurationRevision, AircraftConfigurationRevisionDTO> {

    @Mapping(source = "aircraftConfiguration.id", target = "aircraftConfigurationId")
    AircraftConfigurationRevisionDTO toDTO(AircraftConfigurationRevision entity);

    @Mapping(target = "aircraftConfiguration", ignore = true)
    AircraftConfigurationRevision toEntity(AircraftConfigurationRevisionDTO dto);

    @Mapping(target = "aircraftConfiguration", ignore = true)
    void updateEntityFromDto(AircraftConfigurationRevisionDTO dto, @MappingTarget AircraftConfigurationRevision entity);
}
