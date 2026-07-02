package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.StandardMessageIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;
import com.codeshare.airline.master.messaging.mappers.StandardMessageIdentifierMapper;
import com.codeshare.airline.master.messaging.repository.StandardMessageIdentifierRepository;
import com.codeshare.airline.master.messaging.service.StandardMessageIdentifierService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StandardMessageIdentifierServiceImpl
        extends BaseServiceImpl<StandardMessageIdentifier, StandardMessageIdentifierDTO, UUID>
        implements StandardMessageIdentifierService {

    public StandardMessageIdentifierServiceImpl(StandardMessageIdentifierRepository repository,
                                                StandardMessageIdentifierMapper mapper) {
        super(repository, mapper);
    }
}
