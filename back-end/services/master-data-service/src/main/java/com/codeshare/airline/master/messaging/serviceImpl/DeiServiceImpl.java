package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.DeiDTO;
import com.codeshare.airline.master.messaging.entities.DeiRegistry;
import com.codeshare.airline.master.messaging.repository.DeiRepository;
import com.codeshare.airline.master.messaging.service.DeiService;
import com.codeshare.airline.master.messaging.mappers.DeiMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeiServiceImpl
        extends BaseServiceImpl<DeiRegistry, DeiDTO, Long>
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