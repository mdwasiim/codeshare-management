package com.codeshare.airline.inbound.services.ssim;

import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.inbound.mappers.ssim.SsimAggregateMapper;
import com.codeshare.airline.inbound.repositories.ssim.SsimFileMetaDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimPersistenceService implements SsimPersistenceService {

    private final SsimFileMetaDataRepository fileRepository;
    private final SsimAggregateMapper aggregateMapper;

    @Override
    public void saveBatch(SSIMMessageDTO context, SsimMetaDataDTO metadata) {
        if (context == null || metadata == null) {
            return;
        }

        SsimFileMetaDataEntity logicalFile = resolveLogicalFile(context, metadata);
        aggregateMapper.toEntity(context, logicalFile);
        fileRepository.save(logicalFile);
    }

    private SsimFileMetaDataEntity resolveLogicalFile(SSIMMessageDTO context, SsimMetaDataDTO metadata) {
        SsimFileMetaDataEntity root = fileRepository.findById(metadata.getId())
                .orElseThrow(() -> new IllegalStateException("SSIM file not found for id=" + metadata.getId()));

        String airlineCode = resolveAirlineCode(context, metadata);
        String checksum = logicalChecksum(metadata, context, airlineCode);

        if (isEmptyLogicalFile(root)) {
            applyLogicalMetadata(root, metadata, airlineCode, checksum, root.getFileId());
            return root;
        }

        return fileRepository.findFirstByLoadIdAndAirlineCodeAndChecksum(metadata.getLoadId(), airlineCode, checksum)
                .orElseGet(() -> fileRepository.save(newLogicalFile(metadata, airlineCode, checksum)));
    }

    private SsimFileMetaDataEntity newLogicalFile(SsimMetaDataDTO source, String airlineCode, String checksum) {
        SsimFileMetaDataEntity entity = new SsimFileMetaDataEntity();
        applyLogicalMetadata(entity, source, airlineCode, checksum, UUID.randomUUID());
        return entity;
    }

    private void applyLogicalMetadata(
            SsimFileMetaDataEntity target,
            SsimMetaDataDTO source,
            String airlineCode,
            String checksum,
            UUID fileId
    ) {
        target.setFileId(fileId);
        target.setLoadId(source.getLoadId());
        target.setAirlineCode(airlineCode);
        target.setFileName(source.getFileName());
        target.setSourceType(source.getSourceType());
        target.setMessageType(source.getMessageType());
        target.setScheduleProfile(source.getScheduleProfile());
        target.setTimeMode(source.getTimeMode());
        target.setFileSizeBytes(source.getFileSizeBytes());
        target.setTotalRecordCount(source.getTotalRecordCount());
        target.setChecksum(checksum);
        target.setProcessingStatus(source.getProcessingStatus());
        target.setReceivedTimestamp(source.getReceivedAt() != null ? source.getReceivedAt() : Instant.now());
    }

    private boolean isEmptyLogicalFile(SsimFileMetaDataEntity file) {
        return file.getHeader() == null && file.getCarrier() == null && file.getTrailer() == null;
    }

    private String resolveAirlineCode(SSIMMessageDTO context, SsimMetaDataDTO metadata) {
        if (context.getCarrier() != null && hasText(context.getCarrier().getAirlineCode())) {
            return context.getCarrier().getAirlineCode().trim();
        }
        if (hasText(context.getAirlineCode())) {
            return context.getAirlineCode().trim();
        }
        return metadata.getAirlineCode();
    }

    private String logicalChecksum(SsimMetaDataDTO metadata, SSIMMessageDTO context, String airlineCode) {
        String value = String.join("|",
                text(metadata.getChecksum()),
                text(airlineCode),
                context.getCarrier() != null ? text(context.getCarrier().getRecordSerialNumber()) : "",
                context.getTrailer() != null ? text(context.getTrailer().getRecordSerialNumber()) : ""
        );
        return sha256(value);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String text(String value) {
        return value == null ? "" : value.trim();
    }
}
