package com.codeshare.airline.master.aircraft.mappers;

import com.codeshare.airline.master.aircraft.entities.CabinCrewEmployer;
import com.codeshare.airline.platform.core.dto.master.aircraft.CabinCrewOperatorDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface CabinCrewOperatorMapper
        extends CSMGenericMapper<CabinCrewEmployer, CabinCrewOperatorDTO> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "airline.id", target = "airlineId")
    CabinCrewOperatorDTO toDTO(CabinCrewEmployer entity);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    CabinCrewEmployer toEntity(CabinCrewOperatorDTO dto);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(CabinCrewOperatorDTO dto, @MappingTarget CabinCrewEmployer entity);
}
