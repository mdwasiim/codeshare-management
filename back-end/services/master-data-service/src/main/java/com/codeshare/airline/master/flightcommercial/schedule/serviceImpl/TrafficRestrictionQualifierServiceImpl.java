package com.codeshare.airline.master.flightcommercial.schedule.serviceImpl;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TrafficRestrictionQualifierDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TrafficRestrictionCode;
import com.codeshare.airline.master.flightcommercial.schedule.entities.TrafficRestrictionQualifier;
import com.codeshare.airline.master.flightcommercial.schedule.mappers.TrafficRestrictionQualifierMapper;
import com.codeshare.airline.master.flightcommercial.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flightcommercial.schedule.repository.TrafficRestrictionQualifierRepository;
import com.codeshare.airline.master.flightcommercial.schedule.service.TrafficRestrictionQualifierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrafficRestrictionQualifierServiceImpl
        extends BaseServiceImpl<TrafficRestrictionQualifier, TrafficRestrictionQualifierDTO, UUID>
        implements TrafficRestrictionQualifierService {

    private final TrafficRestrictionCodeRepository trafficRestrictionCodeRepository;

    public TrafficRestrictionQualifierServiceImpl(TrafficRestrictionQualifierRepository repository,
                                                  TrafficRestrictionQualifierMapper mapper,
                                                  TrafficRestrictionCodeRepository trafficRestrictionCodeRepository) {
        super(repository, mapper);
        this.trafficRestrictionCodeRepository = trafficRestrictionCodeRepository;
    }

    private TrafficRestrictionCode getTrafficRestrictionCode(UUID id) {
        if (id == null) {
            return null;
        }

        return trafficRestrictionCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Traffic restriction code not found"));
    }

    @Override
    public TrafficRestrictionQualifierDTO create(TrafficRestrictionQualifierDTO dto) {
        TrafficRestrictionQualifier entity = mapper.toEntity(dto);
        entity.setTrafficRestrictionCode(getTrafficRestrictionCode(dto.getTrafficRestrictionCodeId()));
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public TrafficRestrictionQualifierDTO update(UUID id, TrafficRestrictionQualifierDTO dto) {
        TrafficRestrictionQualifier existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Traffic restriction qualifier not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setTrafficRestrictionCode(getTrafficRestrictionCode(dto.getTrafficRestrictionCodeId()));
        return mapper.toDTO(repository.save(existing));
    }
}
