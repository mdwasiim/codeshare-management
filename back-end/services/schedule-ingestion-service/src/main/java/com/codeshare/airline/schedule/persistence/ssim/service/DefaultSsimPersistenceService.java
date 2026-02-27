package com.codeshare.airline.schedule.persistence.ssim.service;


import com.codeshare.airline.schedule.persistence.ssim.entity.*;
import com.codeshare.airline.schedule.persistence.ssim.repository.*;
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
    private final SsimInboundHeaderRepository headerRepository;
    private final SsimInboundCarrierRepository carrierRepository;
    private final SsimInboundTrailerRepository trailerRepository;
    private final SsimInboundFileRepository fileRepository;
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


    @Override
    public void saveHeader(SsimInboundHeader header) {
        headerRepository.save(header);
    }

    @Override
    public void saveCarrier(SsimInboundCarrier carrier) {
        SsimInboundCarrier ssimInboundCarrier = carrierRepository.save(carrier);

        com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile inboundFile = ssimInboundCarrier.getInboundFile();
        inboundFile.setAirlineCode(carrier.getAirlineCode().trim());
        fileRepository.save(inboundFile);
    }

    @Override
    public void saveTrailer(SsimInboundTrailer trailer) {
        trailerRepository.save(trailer);
    }



}
