package com.codeshare.airline.persistence.persistence.service;


import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
public abstract class BaseServiceImpl<E extends CSMDataAbstractEntity, D, ID>  implements BaseService<D, ID> {

    protected final CSMDataBaseRepository<E, ID> repository;
    protected final CSMGenericMapper<E, D> mapper;

    @Override
    public D create(D dto) {
        E entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public D update(ID id, D dto) {
        E existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public D getById(ID id) {
        return mapper.toDTO(
                repository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        return mapper.toDTOList(repository.findAll());
    }


    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}