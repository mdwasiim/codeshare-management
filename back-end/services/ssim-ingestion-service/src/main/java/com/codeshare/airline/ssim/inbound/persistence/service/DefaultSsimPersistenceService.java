package com.codeshare.airline.ssim.inbound.persistence.service;


import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundFlightLegRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundSegmentDeiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultSsimPersistenceService implements SsimPersistenceService {

    private final SsimInboundFlightLegRepository flightLegRepo;
    private final SsimInboundSegmentDeiRepository segmentDeiRepo;

    /**
     * Persist ONE flight atomically.
     * This method is called repeatedly by the loader.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(SsimInboundFlightLeg flight,List<SsimInboundSegmentDei> deis) {
       if(deis!=null){
           flight.setSegmentDeis(deis);
       }
        flightLegRepo.save(flight);
    }
}
