package com.codeshare.airline.ssim.persistence.inbound.repository;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SsimInboundHeaderRepository
        extends JpaRepository<SsimInboundHeader, Long> {

    Optional<SsimInboundHeader> findByFile_FileId(String fileId);
}
