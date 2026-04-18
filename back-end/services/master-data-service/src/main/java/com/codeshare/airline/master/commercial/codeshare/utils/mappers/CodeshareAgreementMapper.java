package com.codeshare.airline.master.commercial.codeshare.utils.mappers;

import com.codeshare.airline.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.master.commercial.codeshare.eitities.CodeshareAgreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface CodeshareAgreementMapper
        extends CSMGenericMapper<CodeshareAgreement, CodeshareAgreementDTO> {

    @Mapping(source = "marketingAirline.id", target = "marketingAirlineId")
    @Mapping(source = "operatingAirline.id", target = "operatingAirlineId")
    CodeshareAgreementDTO toDTO(CodeshareAgreement entity);
}