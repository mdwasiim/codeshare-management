package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.DataElementIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.DataElementIdentifier;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;
import com.codeshare.airline.master.messaging.mappers.DataElementIdentifierMapper;
import com.codeshare.airline.master.messaging.repository.DataElementIdentifierRepository;
import com.codeshare.airline.master.messaging.repository.StandardMessageIdentifierRepository;
import com.codeshare.airline.master.messaging.service.DataElementIdentifierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class DataElementIdentifierServiceImpl
        extends BaseServiceImpl<DataElementIdentifier, DataElementIdentifierDTO, Long>
        implements DataElementIdentifierService {

    private final StandardMessageIdentifierRepository standardMessageIdentifierRepository;

    public DataElementIdentifierServiceImpl(DataElementIdentifierRepository repository,
                                            DataElementIdentifierMapper mapper,
                                            StandardMessageIdentifierRepository standardMessageIdentifierRepository) {
        super(repository, mapper);
        this.standardMessageIdentifierRepository = standardMessageIdentifierRepository;
    }

    private StandardMessageIdentifier getStandardMessageIdentifier(Long id) {
        if (id == null) {
            return null;
        }

        return standardMessageIdentifierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Standard message identifier not found"));
    }

    @Override
    public DataElementIdentifierDTO create(DataElementIdentifierDTO dto) {
        DataElementIdentifier entity = mapper.toEntity(dto);
        entity.setStandardMessageIdentifier(getStandardMessageIdentifier(dto.getStandardMessageIdentifierId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public DataElementIdentifierDTO update(Long id, DataElementIdentifierDTO dto) {
        DataElementIdentifier existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Data element identifier not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setStandardMessageIdentifier(getStandardMessageIdentifier(dto.getStandardMessageIdentifierId()));

        return mapper.toDTO(repository.save(existing));
    }
}
