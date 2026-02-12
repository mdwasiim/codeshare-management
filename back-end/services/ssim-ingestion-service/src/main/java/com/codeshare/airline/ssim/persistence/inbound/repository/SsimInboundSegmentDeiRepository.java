package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundSegmentDei;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SsimInboundSegmentDeiRepository
        extends JpaRepository<SsimInboundSegmentDei, Long> {

    List<SsimInboundSegmentDei> findByFlightLeg_Id(Long flightLegId);
}
