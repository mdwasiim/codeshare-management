package com.codeshare.airline.master.airline.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.AllianceDTO;
import com.codeshare.airline.master.airline.entities.Alliance;
import com.codeshare.airline.master.airline.mappers.AllianceMapper;
import com.codeshare.airline.master.airline.repository.AllianceRepository;
import com.codeshare.airline.master.airline.service.AllianceService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.georegion.entities.City;
import com.codeshare.airline.master.georegion.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AllianceServiceImpl extends BaseServiceImpl<Alliance, AllianceDTO, UUID> implements AllianceService {
    private final CityRepository cityRepository;

    public AllianceServiceImpl(AllianceRepository repository, AllianceMapper mapper, CityRepository cityRepository) {
        super(repository, mapper);
        this.cityRepository = cityRepository;
    }

    private City city(UUID id) {
        return id == null ? null : cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Headquarters city not found"));
    }

    @Override
    public AllianceDTO create(AllianceDTO dto) {
        Alliance entity = mapper.toEntity(dto);
        entity.setHeadquartersCity(city(dto.getHeadquartersCityId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AllianceDTO update(UUID id, AllianceDTO dto) {
        Alliance existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Alliance not found"));
        mapper.updateEntityFromDto(dto, existing);
        existing.setHeadquartersCity(city(dto.getHeadquartersCityId()));
        return mapper.toDTO(repository.save(existing));
    }
}
