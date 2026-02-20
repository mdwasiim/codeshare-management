package com.codeshare.airline.data.ssim.serviceImpl;

import com.codeshare.airline.core.dto.ssim.MessageTypeDTO;
import com.codeshare.airline.data.ssim.eitities.MessageType;
import com.codeshare.airline.data.ssim.repository.MessageTypeRepository;
import com.codeshare.airline.data.ssim.service.MessageTypeService;
import com.codeshare.airline.data.ssim.utils.mappers.MessageTypeMapper;
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