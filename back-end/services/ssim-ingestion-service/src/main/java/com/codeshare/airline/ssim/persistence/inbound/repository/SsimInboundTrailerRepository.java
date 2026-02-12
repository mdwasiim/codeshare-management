package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SsimInboundTrailerRepository
        extends JpaRepository<SsimInboundTrailer, Long> {

    Optional<SsimInboundTrailer> findByFile_FileId(String fileId);
}
