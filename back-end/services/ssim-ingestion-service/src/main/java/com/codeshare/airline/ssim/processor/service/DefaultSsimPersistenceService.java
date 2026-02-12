package com.codeshare.airline.ssim.processor.service;


import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundSegmentDei;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundFlightLegRepository;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundSegmentDeiRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;

    /**
     * Persist ONE flight atomically.
     * This method is called repeatedly by the loader.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(SsimInboundFlightLeg flight,List<SsimInboundSegmentDei> deis) {
        // persist flight
        em.persist(flight);

        // persist DEIs
        for (SsimInboundSegmentDei dei : deis) {
            em.persist(dei);
        }

        // critical for GB-scale ingestion
        em.flush();
        em.clear();
    }
}
