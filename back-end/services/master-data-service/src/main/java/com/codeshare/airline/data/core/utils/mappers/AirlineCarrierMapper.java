package com.codeshare.airline.data.core.utils.mappers;

import com.codeshare.airline.core.dto.georegion.AirlineCarrierDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
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