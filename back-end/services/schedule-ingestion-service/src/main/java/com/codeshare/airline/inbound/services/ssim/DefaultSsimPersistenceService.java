package com.codeshare.airline.inbound.services.ssim;

import com.codeshare.airline.inbound.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.inbound.entities.ssim.SsimCarrierEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimFlightEntity;
import com.codeshare.airline.inbound.mappers.ssim.SsimAggregateMapper;
import com.codeshare.airline.inbound.mappers.ssim.SsimFlightMapper;
import com.codeshare.airline.inbound.repositories.ssim.SsimFileMetaDataRepository;
import com.codeshare.airline.inbound.repositories.ssim.SsimFlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimPersistenceService implements SsimPersistenceService {

    private final SsimFileMetaDataRepository fileRepository;
    private final SsimFlightRepository flightRepository;
    private final SsimAggregateMapper aggregateMapper;
    private final SsimFlightMapper flightMapper;

    @Override
    public void saveBatch(SSIMMessageDTO context, SsimMetaDataDTO metadata) {
        if (context == null || metadata == null) {
            return;
        }

        SsimFileMetaDataEntity logicalFile = resolveLogicalFile(context, metadata);
        aggregateMapper.toEntity(metadataOnly(context), logicalFile);
        fileRepository.save(logicalFile);

        saveFlights(context.getFlights(), logicalFile.getCarrier());
    }

    private void saveFlights(List<SsimFlightDTO> flights, SsimCarrierEntity carrier) {
        if (flights == null || flights.isEmpty() || carrier == null) {
            return;
        }

        List<SsimFlightEntity> entities = new ArrayList<>(flights.size());
        for (SsimFlightDTO flight : flights) {
            entities.add(flightMapper.toEntity(flight, carrier));
        }
        flightRepository.saveAll(entities);
    }

    private SSIMMessageDTO metadataOnly(SSIMMessageDTO source) {
        return SSIMMessageDTO.builder()
                .airlineCode(source.getAirlineCode())
                .flightNumber(source.getFlightNumber())
                .header(source.getHeader())
                .carrier(source.getCarrier())
                .trailer(source.getTrailer())
                .flights(List.of())
                .build();
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
                context.getCarrier() != null ? text(context.getCarrier().getRecordSerialNumber()) : ""
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
