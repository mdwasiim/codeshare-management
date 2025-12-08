package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.services.exceptions.ResourceNotFoundException;
import com.codeshare.airline.common.tenant.model.TenantDataSourceDTO;
import com.codeshare.airline.tenant.entities.TenantDataSource;
import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import com.codeshare.airline.tenant.service.TenantDataSourceService;
import com.codeshare.airline.tenant.utils.mappers.TenantDataSourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TenantDataSourceServiceImpl implements TenantDataSourceService {

    private final TenantDataSourceRepository repository;
    private final TenantDataSourceMapper mapper;


    // ------------------------------------------------------------------------
    // CREATE NEW TENANT DATA SOURCE
    // ------------------------------------------------------------------------
    @Override
    public TenantDataSourceDTO create(TenantDataSourceDTO dto) {

        TenantDataSource entity = mapper.toEntity(dto);
        TenantDataSource saved = repository.save(entity);

        return mapper.toDTO(saved);
    }


    // ------------------------------------------------------------------------
    // UPDATE EXISTING DATA SOURCE
    // ------------------------------------------------------------------------
    @Override
    public TenantDataSourceDTO update(UUID id, TenantDataSourceDTO dto) {

        TenantDataSource entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataSource not found: " + id));

        // Generic MapStruct updater method (only updates non-null fields)
        mapper.updateEntityFromDto(dto, entity);

        return mapper.toDTO(repository.save(entity));
    }


    // ------------------------------------------------------------------------
    // FETCH DATA SOURCE BY ID
    // ------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public TenantDataSourceDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("DataSource not found: " + id));
    }


    // ------------------------------------------------------------------------
    // FETCH ALL DATA SOURCES
    // ------------------------------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<TenantDataSourceDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    // ------------------------------------------------------------------------
    // DELETE DATA SOURCE BY ID
    // ------------------------------------------------------------------------
    @Override
    public void delete(UUID id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DataSource not found: " + id);
        }

        repository.deleteById(id);
    }
}
