package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.master.aircraft.entities.CockpitCrewEmployer;
import com.codeshare.airline.platform.core.dto.master.aircraft.CockpitCrewOperatorDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CockpitCrewOperatorMapper
        extends CSMGenericMapper<CockpitCrewEmployer, CockpitCrewOperatorDTO> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "airline.id", target = "airlineId")
    CockpitCrewOperatorDTO toDTO(CockpitCrewEmployer entity);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    CockpitCrewEmployer toEntity(CockpitCrewOperatorDTO dto);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(CockpitCrewOperatorDTO dto, @MappingTarget CockpitCrewEmployer entity);
}
