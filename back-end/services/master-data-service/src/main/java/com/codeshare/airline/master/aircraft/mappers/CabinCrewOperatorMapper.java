package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.core.dto.master.aircraft.CabinCrewOperatorDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.aircraft.entities.CabinCrewOperator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CabinCrewOperatorMapper
        extends CSMGenericMapper<CabinCrewOperator, CabinCrewOperatorDTO> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "airline.id", target = "airlineId")
    CabinCrewOperatorDTO toDTO(CabinCrewOperator entity);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    CabinCrewOperator toEntity(CabinCrewOperatorDTO dto);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(CabinCrewOperatorDTO dto, @MappingTarget CabinCrewOperator entity);
}
