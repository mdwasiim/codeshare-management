package com.codeshare.airline.ingestion.persistence.services.ssim;


import com.codeshare.airline.ingestion.persistence.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.ingestion.persistence.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.ingestion.persistence.mappers.ssim.SsimAggregateMapper;
import com.codeshare.airline.ingestion.persistence.repositories.ssim.SsimFileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimPersistenceService implements SsimPersistenceService {

    private final SsimFileMetaDataRepository fileRepository;
    private final SsimAggregateMapper aggregateMapper;

    @Override
    public void saveBatch(SSIMMessageDTO context, SsimMetaDataDTO metadata) {

        if (context == null || metadata == null) return;

        SsimFileMetaDataEntity file =
                fileRepository.findById(metadata.getId())
                        .orElseThrow(() -> new IllegalStateException(
                                "SSIM file not found for id=" + metadata.getId()
                        ));

        // 🔥 Build full graph
        aggregateMapper.toEntity(context, file);

        // 🔥 SINGLE SAVE
        fileRepository.save(file);
    }
}
