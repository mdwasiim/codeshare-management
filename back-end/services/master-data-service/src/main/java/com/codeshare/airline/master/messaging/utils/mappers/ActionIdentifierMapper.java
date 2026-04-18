package com.codeshare.airline.messaging.utils.mappers;

import com.codeshare.airline.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.mapper.CSMGenericMapper;
import com.codeshare.airline.mapper.CSMMapperConfig;
import com.codeshare.airline.messaging.eitities.ActionIdentifier;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ActionIdentifierMapper
        extends CSMGenericMapper<ActionIdentifier, ActionIdentifierDTO> {

}