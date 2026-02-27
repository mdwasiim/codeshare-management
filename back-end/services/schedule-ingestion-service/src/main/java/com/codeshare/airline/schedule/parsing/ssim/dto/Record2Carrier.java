package com.codeshare.airline.schedule.parsing.ssim.dto;


import com.codeshare.airline.schedule.domain.common.TimeMode;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundCarrier;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class Record2Carrier {

    SsimParsedFile inboundFile;

    UUID id;
    UUID fileId;

    // Core
    String recordType;
    TimeMode timeMode;
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

    public static Record2Carrier toDto(SsimInboundCarrier entity) {

        if (entity == null) {
            return null;
        }

        return Record2Carrier.builder()
                .id(entity.getId())

                .inboundFile(
                        entity.getInboundFile() != null
                                ? SsimParsedFile.toDto(entity.getInboundFile())
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
