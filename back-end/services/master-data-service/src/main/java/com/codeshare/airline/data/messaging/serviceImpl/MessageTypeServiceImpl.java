package com.codeshare.airline.data.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.MessageTypeDTO;
import com.codeshare.airline.data.messaging.eitities.MessageType;
import com.codeshare.airline.data.messaging.repository.MessageTypeRepository;
import com.codeshare.airline.data.messaging.service.MessageTypeService;
import com.codeshare.airline.data.messaging.utils.mappers.MessageTypeMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageTypeServiceImpl
        extends BaseServiceImpl<MessageType, MessageTypeDTO, UUID>
        implements MessageTypeService {

    public MessageTypeServiceImpl(MessageTypeRepository repository,
                                  MessageTypeMapper mapper) {
        super(repository, mapper);
    }
}