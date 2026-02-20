package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimTimeMode;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SsimInboundFileDTO {

    SsimInboundCarrierDTO ssimInboundCarrier;

    /* ===================== IDENTITY ===================== */

    String fileId;
    UUID loadId;
    String externalReference;

    /* ===================== OWNERSHIP ===================== */

    String airlineCode;
    String airlineName;

    /* ===================== ORIGIN ===================== */

    String fileName;
    SsimSourceType sourceType;
    String sourceSystem;

    /* ===================== FILE CHARACTERISTICS ===================== */

    Long fileSizeBytes;
    Integer totalRecords;
    String checksum;
    String characterSet;

    /* ===================== SSIM CLASSIFICATION ===================== */

    SsimProfile ssimProfile;
    String ssimVersion;
    SsimTimeMode timeMode;

    /* ===================== PROCESSING ===================== */

    SsimProcessingStatus processingStatus;

    Boolean superseding;
    String supersedesFileId;

    /* ===================== TIMESTAMPS ===================== */

    Instant receivedAt;
    Instant parsedAt;
    Instant validatedAt;
    Instant storedAt;
    Instant failedAt;

    /* ===================== ERROR ===================== */

    String errorCode;
    String errorMessage;

    /* ===================== AUDIT ===================== */

    String ingestedBy;
    String remarks;


    public static SsimInboundFileDTO toDto(SsimInboundFile entity) {

        return SsimInboundFileDTO.builder()
                .fileId(String.valueOf(entity.getId()))
                .loadId(entity.getLoadId())
                .ssimInboundCarrier(SsimInboundCarrierDTO.toDto(entity.getCarrier()))
                .externalReference(entity.getExternalReference())

                .airlineCode(entity.getAirlineCode())
                .airlineName(entity.getAirlineName())

                .fileName(entity.getFileName())
                .sourceType(entity.getSourceType())
                .sourceSystem(entity.getSourceSystem())

                .fileSizeBytes(entity.getFileSizeBytes())
                .totalRecords(entity.getTotalRecordCount())
                .checksum(entity.getChecksum())
                .characterSet(entity.getCharacterSet())

                .ssimProfile(entity.getSsimProfile())
                .ssimVersion(entity.getSsimVersion())
                .timeMode(entity.getTimeMode())

                .processingStatus(entity.getProcessingStatus())
                .superseding(entity.getIsSuperseding())
                .supersedesFileId(entity.getSupersededFileId())

                .receivedAt(entity.getReceivedTimestamp())
                .parsedAt(entity.getParsedTimestamp())
                .validatedAt(entity.getValidatedTimestamp())
                .storedAt(entity.getStoredTimestamp())
                .failedAt(entity.getFailedTimestamp())

                .errorCode(entity.getErrorCode())
                .errorMessage(entity.getErrorMessage())

                .ingestedBy(entity.getIngestedBy())
                .remarks(entity.getRemarks())

                .build();
    }

    public static SsimInboundFile toEntity(SsimInboundFileDTO inboundFileDTO) {

        if (inboundFileDTO == null) {
            return null;
        }

        SsimInboundFile entity = new SsimInboundFile();

        entity.setId(UUID.fromString(inboundFileDTO.getFileId()));
        entity.setLoadId(inboundFileDTO.getLoadId());
        entity.setExternalReference(inboundFileDTO.getExternalReference());

        entity.setAirlineCode(inboundFileDTO.getAirlineCode());
        entity.setAirlineName(inboundFileDTO.getAirlineName());

        entity.setFileName(inboundFileDTO.getFileName());
        entity.setSourceType(inboundFileDTO.getSourceType());
        entity.setSourceSystem(inboundFileDTO.getSourceSystem());

        entity.setFileSizeBytes(inboundFileDTO.getFileSizeBytes());
        entity.setTotalRecordCount(inboundFileDTO.getTotalRecords());
        entity.setChecksum(inboundFileDTO.getChecksum());
        entity.setCharacterSet(inboundFileDTO.getCharacterSet());

        entity.setSsimProfile(inboundFileDTO.getSsimProfile());
        entity.setSsimVersion(inboundFileDTO.getSsimVersion());
        entity.setTimeMode(inboundFileDTO.getTimeMode());

        entity.setProcessingStatus(inboundFileDTO.getProcessingStatus());
        entity.setIsSuperseding(inboundFileDTO.getSuperseding());
        entity.setSupersededFileId(inboundFileDTO.getSupersedesFileId());

        entity.setReceivedTimestamp(inboundFileDTO.getReceivedAt());
        entity.setParsedTimestamp(inboundFileDTO.getParsedAt());
        entity.setValidatedTimestamp(inboundFileDTO.getValidatedAt());
        entity.setStoredTimestamp(inboundFileDTO.getStoredAt());
        entity.setFailedTimestamp(inboundFileDTO.getFailedAt());

        entity.setErrorCode(inboundFileDTO.getErrorCode());
        entity.setErrorMessage(inboundFileDTO.getErrorMessage());

        entity.setIngestedBy(inboundFileDTO.getIngestedBy());
        entity.setRemarks(inboundFileDTO.getRemarks());

        return entity;
    }



}
