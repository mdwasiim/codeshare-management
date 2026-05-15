package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SsimFileMetaDataRepository
        extends CSMDataBaseRepository<SsimFileMetaDataEntity, UUID>,
        JpaSpecificationExecutor<SsimFileMetaDataEntity> {


    Optional<SsimFileMetaDataEntity> findByFileId(UUID fileId);

    Optional<SsimFileMetaDataEntity> findFirstByLoadIdAndAirlineCodeAndChecksum(UUID loadId, String airlineCode, String checksum);

    Optional<SsimFileMetaDataEntity> findByAirlineCodeAndChecksum(String airlineCode, String checksum);

    @Modifying
    @Query("update SsimFileMetaDataEntity f set f.processingStatus = :status where f.id = :id")
    void updateStatus(@Param("id") UUID fileId,
                      @Param("status") ProcessingStatus status);

}
