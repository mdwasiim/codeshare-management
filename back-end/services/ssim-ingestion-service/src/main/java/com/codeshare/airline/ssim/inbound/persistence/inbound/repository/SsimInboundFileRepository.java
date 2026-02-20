package com.codeshare.airline.ssim.inbound.persistence.inbound.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SsimInboundFileRepository
        extends CSMDataBaseRepository<SsimInboundFile, UUID> {

    @Modifying
    @Query("update SsimInboundFile f set f.processingStatus = :status where f.id = :id")
    void updateStatus(@Param("id") UUID fileId,
                      @Param("status") SsimProcessingStatus status);

    @Modifying
    @Query("""
    update SsimInboundFile f
       set f.ssimProfile = :ssimProfile
     where f.id = :id
""")
    void updateProfile(@Param("id") UUID fileId,
                       @Param("ssimProfile") SsimProfile profile);

}
