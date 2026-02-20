package com.codeshare.airline.data.ssim.serviceImpl;

import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.data.ssim.eitities.Dei;
import com.codeshare.airline.data.ssim.repository.DeiRepository;
import com.codeshare.airline.data.ssim.service.DeiService;
import com.codeshare.airline.data.ssim.utils.mappers.DeiMapper;
import com.codeshare.airline.persistence.persistence.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeiServiceImpl
        extends BaseServiceImpl<Dei, DeiDTO, UUID>
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