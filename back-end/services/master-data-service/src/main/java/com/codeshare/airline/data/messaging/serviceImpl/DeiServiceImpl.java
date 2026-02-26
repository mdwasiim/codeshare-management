package com.codeshare.airline.data.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.data.messaging.eitities.DeiRegistry;
import com.codeshare.airline.data.messaging.repository.DeiRepository;
import com.codeshare.airline.data.messaging.service.DeiService;
import com.codeshare.airline.data.messaging.utils.mappers.DeiMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeiServiceImpl
        extends BaseServiceImpl<DeiRegistry, DeiDTO, UUID>
        implements DeiService {

    private final DeiRepository repository;

    public DeiServiceImpl(
            DeiRepository repository,
            DeiMapper mapper) {

        super(repository, mapper);
        this.repository = repository;
    }

    @Override
    public Optional<DeiDTO> getByDeiNumber(String deiNumber) {

        return repository.findByDeiNumber(deiNumber)
                .map(mapper::toDTO);
    }
}