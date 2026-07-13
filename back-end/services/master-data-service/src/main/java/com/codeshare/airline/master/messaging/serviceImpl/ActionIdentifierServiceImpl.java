package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.ActionIdentifierDTO;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.service.ActionIdentifierService;
import com.codeshare.airline.master.messaging.mappers.ActionIdentifierMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ActionIdentifierServiceImpl
        extends BaseServiceImpl<ActionIdentifier, ActionIdentifierDTO, Long>
        implements ActionIdentifierService {

    public ActionIdentifierServiceImpl(ActionIdentifierRepository repository,
                                       ActionIdentifierMapper mapper) {
        super(repository, mapper);
    }
}