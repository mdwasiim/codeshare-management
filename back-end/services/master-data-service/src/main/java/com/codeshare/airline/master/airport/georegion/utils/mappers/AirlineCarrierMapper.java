package com.codeshare.airline.master.airport.georegion.utils.mappers;

import com.codeshare.airline.dto.airport.georegion.AirlineCarrierDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airport.georegion.eitities.AirlineCarrier;
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