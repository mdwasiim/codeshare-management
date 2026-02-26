package com.codeshare.airline.schedule.persistence.asm.service;


import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFlightLeg;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundSegmentDei;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundFlightLegRepository;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundSegmentDeiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAsmPersistenceService implements AsmPersistenceService {

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
