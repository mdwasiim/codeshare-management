package com.codeshare.airline.data.ssim.serviceImpl;

import com.codeshare.airline.core.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.data.ssim.eitities.ActionIdentifier;
import com.codeshare.airline.data.ssim.repository.ActionIdentifierRepository;
import com.codeshare.airline.data.ssim.service.ActionIdentifierService;
import com.codeshare.airline.data.ssim.utils.mappers.ActionIdentifierMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
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