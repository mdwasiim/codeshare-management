package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftFamilyDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftFamily;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AircraftFamilyMapper
        extends CSMGenericMapper<AircraftFamily, AircraftFamilyDTO> {

    @Mapping(source = "manufacturer.id", target = "manufacturerId")
    AircraftFamilyDTO toDTO(AircraftFamily entity);

    @Mapping(target = "manufacturer", ignore = true)
    AircraftFamily toEntity(AircraftFamilyDTO dto);

    @Mapping(target = "manufacturer", ignore = true)
    void updateEntityFromDto(AircraftFamilyDTO dto, @MappingTarget AircraftFamily entity);
}
