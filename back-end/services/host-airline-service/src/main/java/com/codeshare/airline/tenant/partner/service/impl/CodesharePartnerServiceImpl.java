package com.codeshare.airline.tenant.partner.service.impl;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.tenant.partner.entities.CodesharePartner;
import com.codeshare.airline.tenant.partner.mappers.CodesharePartnerMapper;
import com.codeshare.airline.tenant.partner.repository.CodesharePartnerRepository;
import com.codeshare.airline.tenant.partner.service.CodesharePartnerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CodesharePartnerServiceImpl implements CodesharePartnerService {

    private final CodesharePartnerRepository repository;
    private final CodesharePartnerMapper mapper;

    @Override
    public CodesharePartnerDTO create(CodesharePartnerDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public CodesharePartnerDTO update(UUID id, CodesharePartnerDTO dto) {
        CodesharePartner existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerDTO getById(UUID id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
