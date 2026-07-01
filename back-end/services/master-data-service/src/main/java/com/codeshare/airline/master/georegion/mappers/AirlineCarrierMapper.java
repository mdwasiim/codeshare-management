package com.codeshare.airline.master.georegion.mappers;

import com.codeshare.airline.core.dto.airport.georegion.AirlineCarrierDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.georegion.eitities.AirlineCarrier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = CSMMapperConfig.class
)
public interface AirlineCarrierMapper
        extends CSMGenericMapper<AirlineCarrier, AirlineCarrierDTO> {

    @Mapping(source = "country.id", target = "countryId")
    AirlineCarrierDTO toDTO(AirlineCarrier entity);
}