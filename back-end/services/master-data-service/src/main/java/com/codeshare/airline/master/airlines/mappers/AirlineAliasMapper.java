package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.core.dto.master.airline.AirlineAliasDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.entities.AirlineAlias;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirlineAliasMapper extends CSMGenericMapper<AirlineAlias, AirlineAliasDTO> {
    @Mapping(source = "airline.id", target = "airlineId")
    AirlineAliasDTO toDTO(AirlineAlias entity);

    @Mapping(target = "airline", ignore = true)
    AirlineAlias toEntity(AirlineAliasDTO dto);

    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(AirlineAliasDTO dto, @MappingTarget AirlineAlias entity);
}
