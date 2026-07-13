package com.codeshare.airline.master.flight.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionQualifierDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionCode;
import com.codeshare.airline.master.flight.schedule.entities.TrafficRestrictionQualifier;
import com.codeshare.airline.master.flight.schedule.mappers.TrafficRestrictionQualifierMapper;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionCodeRepository;
import com.codeshare.airline.master.flight.schedule.repository.TrafficRestrictionQualifierRepository;
import com.codeshare.airline.master.flight.schedule.service.TrafficRestrictionQualifierService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class TrafficRestrictionQualifierServiceImpl
        extends BaseServiceImpl<TrafficRestrictionQualifier, TrafficRestrictionQualifierDTO, Long>
        implements TrafficRestrictionQualifierService {

    private final TrafficRestrictionCodeRepository trafficRestrictionCodeRepository;

    public TrafficRestrictionQualifierServiceImpl(TrafficRestrictionQualifierRepository repository,
                                                  TrafficRestrictionQualifierMapper mapper,
                                                  TrafficRestrictionCodeRepository trafficRestrictionCodeRepository) {
        super(repository, mapper);
        this.trafficRestrictionCodeRepository = trafficRestrictionCodeRepository;
    }

    private TrafficRestrictionCode getTrafficRestrictionCode(Long id) {
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
    public TrafficRestrictionQualifierDTO update(Long id, TrafficRestrictionQualifierDTO dto) {
        TrafficRestrictionQualifier existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Traffic restriction qualifier not found"));

        mapper.updateEntityFromDto(dto, existing);
        existing.setTrafficRestrictionCode(getTrafficRestrictionCode(dto.getTrafficRestrictionCodeId()));
        return mapper.toDTO(repository.save(existing));
    }
}
