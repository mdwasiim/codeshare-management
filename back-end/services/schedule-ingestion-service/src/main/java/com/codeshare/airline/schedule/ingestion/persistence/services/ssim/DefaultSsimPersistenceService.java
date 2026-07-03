package com.codeshare.airline.schedule.ingestion.persistence.services.ssim;

import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimCarrierEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFlightEntity;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimAggregateMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimFlightMapper;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim.SsimFileMetaDataRepository;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim.SsimFlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimPersistenceService implements SsimPersistenceService {

    private final SsimFileMetaDataRepository fileRepository;
    private final SsimFlightRepository flightRepository;
    private final SsimAggregateMapper aggregateMapper;
    private final SsimFlightMapper flightMapper;

    @Override
    public void saveBatch(SSIMMessageDTO message, SsimMetaDataDTO metadata) {
        if (message == null || metadata == null) {
            return;
        }

        SsimFileMetaDataEntity file = resolveTargetFile(message, metadata);
        saveFileEnvelope(message, file);
        saveFlights(message.getFlights(), file.getCarrier());
    }

    private void saveFileEnvelope(SSIMMessageDTO message, SsimFileMetaDataEntity file) {
        aggregateMapper.toEntity(envelopeOnly(message), file);
        fileRepository.save(file);
    }

    private void saveFlights(List<SsimFlightDTO> flights, SsimCarrierEntity carrier) {
        if (flights == null || flights.isEmpty() || carrier == null) {
            return;
        }

        Set<String> existingFlightKeys = existingFlightKeys(carrier);
        List<SsimFlightEntity> flightEntities = new ArrayList<>(flights.size());
        for (SsimFlightDTO flight : flights) {
            if (!existingFlightKeys.add(flightKey(flight))) {
                continue;
            }
            flightEntities.add(flightMapper.toEntity(flight, carrier));
        }

        if (!flightEntities.isEmpty()) {
            flightRepository.saveAll(flightEntities);
        }
    }

    private SSIMMessageDTO envelopeOnly(SSIMMessageDTO source) {
        return SSIMMessageDTO.builder()
                .airlineCode(source.getAirlineCode())
                .flightNumber(source.getFlightNumber())
                .header(source.getHeader())
                .carrier(source.getCarrier())
                .trailer(source.getTrailer())
                .flights(List.of())
                .build();
    }

    private SsimFileMetaDataEntity resolveTargetFile(SSIMMessageDTO message, SsimMetaDataDTO metadata) {
        SsimFileMetaDataEntity root = fileRepository.findByIdForUpdate(metadata.getId())
                .orElseThrow(() -> new IllegalStateException("SSIM file not found for id=" + metadata.getId()));

        String airlineCode = resolveAirlineCode(message, metadata);
        String checksum = logicalChecksum(metadata, message, airlineCode);

        if (hasNoSavedEnvelope(root)) {
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

    private boolean hasNoSavedEnvelope(SsimFileMetaDataEntity file) {
        return file.getHeader() == null && file.getCarrier() == null && file.getTrailer() == null;
    }

    private Set<String> existingFlightKeys(SsimCarrierEntity carrier) {
        if (carrier.getFlights() == null || carrier.getFlights().isEmpty()) {
            return new HashSet<>();
        }

        return carrier.getFlights().stream()
                .map(this::flightKey)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private String flightKey(SsimFlightEntity flight) {
        if (flight == null) {
            return "";
        }
        return String.join("|",
                text(flight.getAirlineCode()),
                text(flight.getFlightNumber()),
                text(flight.getOperationalSuffix()),
                text(flight.getItineraryVariationIdentifier()),
                flight.getLegSequenceNumber() == null ? "" : flight.getLegSequenceNumber().toString()
        );
    }

    private String flightKey(SsimFlightDTO flight) {
        if (flight == null) {
            return "";
        }
        return String.join("|",
                text(flight.getAirlineCode()),
                text(flight.getFlightNumber()),
                text(flight.getOperationalSuffix()),
                text(flight.getItineraryVariationIdentifier()),
                flight.getLegSequenceNumber() == null ? "" : flight.getLegSequenceNumber().toString()
        );
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
