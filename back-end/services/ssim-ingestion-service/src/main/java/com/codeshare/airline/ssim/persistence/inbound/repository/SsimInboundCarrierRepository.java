package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundCarrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SsimInboundCarrierRepository
        extends JpaRepository<SsimInboundCarrier, Long> {

    Optional<SsimInboundCarrier> findByFile_FileId(String fileId);
}
