package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.service.ActionIdentifierService;
import com.codeshare.airline.master.messaging.mappers.ActionIdentifierMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActionIdentifierServiceImpl
        extends BaseServiceImpl<ActionIdentifier, ActionIdentifierDTO, UUID>
        implements ActionIdentifierService {

    public ActionIdentifierServiceImpl(ActionIdentifierRepository repository,
                                       ActionIdentifierMapper mapper) {
        super(repository, mapper);
    }
}