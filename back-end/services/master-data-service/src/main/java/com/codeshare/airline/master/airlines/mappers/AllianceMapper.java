package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.platform.core.dto.master.airline.AllianceDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.entities.Alliance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AllianceMapper extends CSMGenericMapper<Alliance, AllianceDTO> {
    @Mapping(source = "headquartersCity.id", target = "headquartersCityId")
    AllianceDTO toDTO(Alliance entity);

    @Mapping(target = "headquartersCity", ignore = true)
    Alliance toEntity(AllianceDTO dto);

    @Mapping(target = "headquartersCity", ignore = true)
    void updateEntityFromDto(AllianceDTO dto, @MappingTarget Alliance entity);
}
