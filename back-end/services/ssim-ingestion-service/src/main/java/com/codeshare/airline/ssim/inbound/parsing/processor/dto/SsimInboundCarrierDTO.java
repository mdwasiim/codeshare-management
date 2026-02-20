package com.codeshare.airline.ssim.inbound.parsing.processor.dto;


import com.codeshare.airline.ssim.inbound.domain.enums.SsimTimeMode;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundCarrier;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SsimInboundCarrierDTO {

    SsimInboundFileDTO inboundFile;

    UUID id;
    UUID fileId;

    // Core
    String recordType;
    SsimTimeMode timeMode;
    String airlineDesignator;

    // Validity
    String season;
    String validityStartRaw;
    String validityEndRaw;

    String creationDateRaw;
    String titleOfData;
    String releaseDateRaw;

    String scheduleStatus;
    String creatorReference;
    String duplicateAirlineMarker;

    String generalInformation;
    String inFlightServiceInformation;
    String electronicTicketingInformation;

    String creationTimeRaw;

    String recordSerialNumber;

    String rawRecord;

    Instant parsedAt;

    public static SsimInboundCarrierDTO toDto(SsimInboundCarrier entity) {

        if (entity == null) {
            return null;
        }

        return SsimInboundCarrierDTO.builder()
                .id(entity.getId())

                .inboundFile(
                        entity.getInboundFile() != null
                                ? SsimInboundFileDTO.toDto(entity.getInboundFile())
                                : null
                )
                .fileId(entity.getId())
                .recordType(entity.getRecordType())
                .timeMode(entity.getTimeMode())
                .airlineDesignator(entity.getAirlineCode())

                .season(entity.getSeason())
                .validityStartRaw(entity.getValidityStartRaw())
                .validityEndRaw(entity.getValidityEndRaw())

                .creationDateRaw(entity.getCreationDateRaw())
                .titleOfData(entity.getTitleOfData())
                .releaseDateRaw(entity.getReleaseDateRaw())

                .scheduleStatus(entity.getScheduleStatus())
                .creatorReference(entity.getCreatorReference())
                .duplicateAirlineMarker(entity.getDuplicateDesignatorMarker())

                .generalInformation(entity.getGeneralInformation())
                .inFlightServiceInformation(entity.getInflightServiceInfo())
                .electronicTicketingInformation(entity.getElectronicTicketingInfo())

                .creationTimeRaw(entity.getCreationTimeRaw())

                .recordSerialNumber(entity.getRecordSerialNumber())
                .rawRecord(entity.getRawRecord())
                .parsedAt(entity.getParsedTimestamp())

                .build();
    }



}
