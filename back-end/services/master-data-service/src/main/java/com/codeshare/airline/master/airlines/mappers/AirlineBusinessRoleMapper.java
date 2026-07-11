package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineBusinessRoleDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.entities.AirlineBusinessRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AirlineBusinessRoleMapper extends CSMGenericMapper<AirlineBusinessRole, AirlineBusinessRoleDTO> {
    @Mapping(source = "airline.id", target = "airlineId")
    AirlineBusinessRoleDTO toDTO(AirlineBusinessRole entity);

    @Mapping(target = "airline", ignore = true)
    AirlineBusinessRole toEntity(AirlineBusinessRoleDTO dto);

    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(AirlineBusinessRoleDTO dto, @MappingTarget AirlineBusinessRole entity);
}
