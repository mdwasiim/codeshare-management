package com.codeshare.airline.messaging.serviceImpl;

import com.codeshare.airline.dto.ssim.DeiDTO;
import com.codeshare.airline.messaging.eitities.DeiRegistry;
import com.codeshare.airline.messaging.repository.DeiRepository;
import com.codeshare.airline.messaging.service.DeiService;
import com.codeshare.airline.messaging.utils.mappers.DeiMapper;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
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