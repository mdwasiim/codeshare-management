package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.platform.core.dto.master.aircraft.CockpitCrewOperatorDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.CockpitCrewOperator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CockpitCrewOperatorMapper
        extends CSMGenericMapper<CockpitCrewOperator, CockpitCrewOperatorDTO> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "airline.id", target = "airlineId")
    CockpitCrewOperatorDTO toDTO(CockpitCrewOperator entity);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    CockpitCrewOperator toEntity(CockpitCrewOperatorDTO dto);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(CockpitCrewOperatorDTO dto, @MappingTarget CockpitCrewOperator entity);
}
