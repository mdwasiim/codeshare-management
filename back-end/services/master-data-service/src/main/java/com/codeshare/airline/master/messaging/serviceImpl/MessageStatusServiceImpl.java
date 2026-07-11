package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.MessageStatusDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.MessageStatus;
import com.codeshare.airline.master.messaging.mappers.MessageStatusMapper;
import com.codeshare.airline.master.messaging.repository.MessageStatusRepository;
import com.codeshare.airline.master.messaging.service.MessageStatusService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MessageStatusServiceImpl
        extends BaseServiceImpl<MessageStatus, MessageStatusDTO, UUID>
        implements MessageStatusService {

    public MessageStatusServiceImpl(MessageStatusRepository repository,
                                    MessageStatusMapper mapper) {
        super(repository, mapper);
    }
}
