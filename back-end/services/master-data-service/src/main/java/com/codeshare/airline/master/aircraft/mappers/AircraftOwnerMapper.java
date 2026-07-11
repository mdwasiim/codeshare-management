package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftOwnerDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.AircraftOwner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AircraftOwnerMapper
        extends CSMGenericMapper<AircraftOwner, AircraftOwnerDTO> {

    @Mapping(source = "country.id", target = "countryId")
    AircraftOwnerDTO toDTO(AircraftOwner entity);

    @Mapping(target = "country", ignore = true)
    AircraftOwner toEntity(AircraftOwnerDTO dto);

    @Mapping(target = "country", ignore = true)
    void updateEntityFromDto(AircraftOwnerDTO dto, @MappingTarget AircraftOwner entity);
}
