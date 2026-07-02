package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.MessagePriorityDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.MessagePriority;
import com.codeshare.airline.master.messaging.mappers.MessagePriorityMapper;
import com.codeshare.airline.master.messaging.repository.MessagePriorityRepository;
import com.codeshare.airline.master.messaging.service.MessagePriorityService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessagePriorityServiceImpl
        extends BaseServiceImpl<MessagePriority, MessagePriorityDTO, UUID>
        implements MessagePriorityService {

    public MessagePriorityServiceImpl(MessagePriorityRepository repository,
                                      MessagePriorityMapper mapper) {
        super(repository, mapper);
    }
}
