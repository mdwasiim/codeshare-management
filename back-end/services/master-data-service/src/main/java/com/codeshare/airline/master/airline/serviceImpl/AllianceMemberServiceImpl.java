package com.codeshare.airline.master.airline.serviceImpl;

import com.codeshare.airline.core.dto.master.airline.AllianceMemberDTO;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import com.codeshare.airline.master.airline.entities.Alliance;
import com.codeshare.airline.master.airline.entities.AllianceMember;
import com.codeshare.airline.master.airline.mappers.AllianceMemberMapper;
import com.codeshare.airline.master.airline.repository.AirlineCarrierRepository;
import com.codeshare.airline.master.airline.repository.AllianceMemberRepository;
import com.codeshare.airline.master.airline.repository.AllianceRepository;
import com.codeshare.airline.master.airline.service.AllianceMemberService;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AllianceMemberServiceImpl extends BaseServiceImpl<AllianceMember, AllianceMemberDTO, UUID> implements AllianceMemberService {
    private final AllianceRepository allianceRepository;
    private final AirlineCarrierRepository airlineCarrierRepository;

    public AllianceMemberServiceImpl(AllianceMemberRepository repository, AllianceMemberMapper mapper,
                                     AllianceRepository allianceRepository, AirlineCarrierRepository airlineCarrierRepository) {
        super(repository, mapper);
        this.allianceRepository = allianceRepository;
        this.airlineCarrierRepository = airlineCarrierRepository;
    }

    private Alliance alliance(UUID id) {
        return id == null ? null : allianceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Alliance not found"));
    }

    private AirlineCarrier airline(UUID id) {
        return id == null ? null : airlineCarrierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Airline not found"));
    }

    private void applyRelations(AllianceMemberDTO dto, AllianceMember entity) {
        entity.setAlliance(alliance(dto.getAllianceId()));
        entity.setAirline(airline(dto.getAirlineId()));
    }

    @Override
    public AllianceMemberDTO create(AllianceMemberDTO dto) {
        AllianceMember entity = mapper.toEntity(dto);
        applyRelations(dto, entity);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public AllianceMemberDTO update(UUID id, AllianceMemberDTO dto) {
        AllianceMember existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Alliance member not found"));
        mapper.updateEntityFromDto(dto, existing);
        applyRelations(dto, existing);
        return mapper.toDTO(repository.save(existing));
    }
}
