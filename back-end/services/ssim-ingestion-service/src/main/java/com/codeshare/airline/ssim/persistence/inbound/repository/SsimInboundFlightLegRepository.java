package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFlightLeg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SsimInboundFlightLegRepository
        extends JpaRepository<SsimInboundFlightLeg, Long> {

    List<SsimInboundFlightLeg> findByFile_FileId(String fileId);

    List<SsimInboundFlightLeg> findByAirlineCodeAndFlightNumber(
            String airlineCode,
            String flightNumber
    );

    void deleteByFile_FileId(String fileId);
}
