package com.codeshare.airline.data.messaging.utils.mappers;

import com.codeshare.airline.core.dto.ssim.MessageTypeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.data.messaging.eitities.MessageType;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface MessageTypeMapper
        extends CSMGenericMapper<MessageType, MessageTypeDTO> {
}
