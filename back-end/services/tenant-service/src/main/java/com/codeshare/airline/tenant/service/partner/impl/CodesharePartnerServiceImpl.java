package com.codeshare.airline.tenant.service.partner.impl;

import com.codeshare.airline.platform.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;
import com.codeshare.airline.tenant.mappers.partner.CodesharePartnerMapper;
import com.codeshare.airline.tenant.repository.partner.CodesharePartnerRepository;
import com.codeshare.airline.tenant.service.partner.CodesharePartnerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public CodesharePartnerDTO update(Long id, CodesharePartnerDTO dto) {
        CodesharePartner existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public CodesharePartnerDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(() -> new EntityNotFoundException("Codeshare partner not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodesharePartnerDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
