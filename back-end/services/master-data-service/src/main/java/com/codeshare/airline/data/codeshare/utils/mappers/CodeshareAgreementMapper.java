package com.codeshare.airline.data.codeshare.utils.mappers;

import com.codeshare.airline.core.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.codeshare.eitities.CodeshareAgreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareAgreementMapper
        extends CSMGenericMapper<CodeshareAgreement, CodeshareAgreementDTO> {

    @Mapping(source = "marketingAirline.id", target = "marketingAirlineId")
    @Mapping(source = "operatingAirline.id", target = "operatingAirlineId")
    CodeshareAgreementDTO toDTO(CodeshareAgreement entity);
}