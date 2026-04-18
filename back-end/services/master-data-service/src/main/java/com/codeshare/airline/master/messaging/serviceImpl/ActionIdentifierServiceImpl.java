package com.codeshare.airline.messaging.serviceImpl;

import com.codeshare.airline.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.messaging.eitities.ActionIdentifier;
import com.codeshare.airline.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.messaging.service.ActionIdentifierService;
import com.codeshare.airline.messaging.utils.mappers.ActionIdentifierMapper;
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