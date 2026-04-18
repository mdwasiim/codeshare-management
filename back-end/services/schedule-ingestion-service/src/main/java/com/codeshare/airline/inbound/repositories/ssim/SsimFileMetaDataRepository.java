package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SsimFileMetaDataRepository
        extends CSMDataBaseRepository<SsimFileMetaDataEntity, UUID> {


    Optional<SsimFileMetaDataEntity> findByFileId(UUID fileId);

    @Modifying
    @Query("update SsimFileMetaDataEntity f set f.processingStatus = :status where f.id = :id")
    void updateStatus(@Param("id") UUID fileId,
                      @Param("status") ProcessingStatus status);

}
