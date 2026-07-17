package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.messaging.ActionCodeDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.ActionCode;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import com.codeshare.airline.master.messaging.mappers.ActionCodeMapper;
import com.codeshare.airline.master.messaging.repository.ActionCodeRepository;
import com.codeshare.airline.master.messaging.repository.ActionIdentifierRepository;
import com.codeshare.airline.master.messaging.service.ActionCodeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ActionCodeServiceImpl
        extends BaseServiceImpl<ActionCode, ActionCodeDTO, Long>
        implements ActionCodeService {

    private final ActionIdentifierRepository actionIdentifierRepository;

    public ActionCodeServiceImpl(ActionCodeRepository repository,
                                 ActionCodeMapper mapper,
                                 ActionIdentifierRepository actionIdentifierRepository) {
        super(repository, mapper);
        this.actionIdentifierRepository = actionIdentifierRepository;
    }

    private ActionIdentifier getActionIdentifier(Long id) {
        if (id == null) {
            return null;
        }

        return actionIdentifierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action identifier not found"));
    }

    @Override
    public ActionCodeDTO create(ActionCodeDTO dto) {
        ActionCode entity = mapper.toEntity(dto);
        entity.setActionIdentifier(getActionIdentifier(dto.getActionIdentifierId()));

        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public ActionCodeDTO update(Long id, ActionCodeDTO dto) {
        ActionCode existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Action code not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setActionIdentifier(getActionIdentifier(dto.getActionIdentifierId()));

        return mapper.toDTO(repository.save(existing));
    }
}
