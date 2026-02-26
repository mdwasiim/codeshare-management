package com.codeshare.airline.data.messaging.utils.mappers;

import com.codeshare.airline.core.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.messaging.eitities.ActionIdentifier;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface ActionIdentifierMapper
        extends CSMGenericMapper<ActionIdentifier, ActionIdentifierDTO> {

}