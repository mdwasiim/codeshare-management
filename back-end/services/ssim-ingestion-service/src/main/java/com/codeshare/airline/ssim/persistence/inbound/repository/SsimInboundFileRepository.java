package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SsimInboundFileRepository
        extends JpaRepository<SsimInboundFile, String> {

    Optional<SsimInboundFile> findByFileId(String fileId);

    Optional<SsimInboundFile> findByAirlineCodeAndChecksum(
            String airlineCode,
            String checksum
    );

    List<SsimInboundFile> findByProcessingStatus(
            SsimProcessingStatus status
    );

    List<SsimInboundFile> findByLoadId(String loadId);
}
