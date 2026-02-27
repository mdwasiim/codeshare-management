package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.persistence.ssim.entity.*;

import java.util.List;

public interface SsimPersistenceService {

    void saveHeader(SsimInboundHeader header);

    void saveCarrier(SsimInboundCarrier carrier);

    void saveTrailer(SsimInboundTrailer trailer);

    void persist(SsimInboundFlightLeg flight,
                 List<SsimInboundSegmentDei> deis);
}
