package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.core.dto.master.messaging.MessageStatusDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.MessageStatus;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface MessageStatusMapper extends CSMGenericMapper<MessageStatus, MessageStatusDTO> {
}
