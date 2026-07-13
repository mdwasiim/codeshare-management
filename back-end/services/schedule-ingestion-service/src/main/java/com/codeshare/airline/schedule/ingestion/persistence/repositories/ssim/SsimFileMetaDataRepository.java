package com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim;

import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SsimFileMetaDataRepository
        extends CSMDataBaseRepository<SsimFileMetaDataEntity, Long>,
        JpaSpecificationExecutor<SsimFileMetaDataEntity> {


    Optional<SsimFileMetaDataEntity> findByFileId(UUID fileId);

    List<SsimFileMetaDataEntity> findAllByLoadId(UUID loadId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from SsimFileMetaDataEntity f where f.id = :id")
    Optional<SsimFileMetaDataEntity> findByIdForUpdate(@Param("id") Long id);

    Optional<SsimFileMetaDataEntity> findFirstByLoadIdAndAirlineCodeAndChecksum(UUID loadId, String airlineCode, String checksum);

    Optional<SsimFileMetaDataEntity> findByAirlineCodeAndChecksum(String airlineCode, String checksum);

    @Modifying
    @Query("update SsimFileMetaDataEntity f set f.processingStatus = :status where f.id = :id")
    void updateStatus(@Param("id") Long fileId,
                      @Param("status") ProcessingStatus status);

}
