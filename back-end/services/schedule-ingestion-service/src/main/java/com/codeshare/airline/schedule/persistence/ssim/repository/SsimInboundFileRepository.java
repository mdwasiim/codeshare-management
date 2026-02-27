package com.codeshare.airline.schedule.persistence.ssim.repository;

import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SsimInboundFileRepository
        extends CSMDataBaseRepository<SsimInboundFile, UUID> {

    @Modifying
    @Query("update SsimParsedFile f set f.processingStatus = :status where f.id = :id")
    void updateStatus(@Param("id") UUID fileId,
                      @Param("status") ProcessingStatus status);

    @Modifying
    @Query("""
    update SsimParsedFile f
       set f.ssimProfile = :ssimProfile
     where f.id = :id
""")
    void updateProfile(@Param("id") UUID fileId,
                       @Param("ssimProfile") ScheduleProfile profile);

}
