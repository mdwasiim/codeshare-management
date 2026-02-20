package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundTrailer;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SsimInboundTrailerDTO {

    /* ===================== IDENTITY ===================== */

    UUID id;
    UUID fileId;

    /* ===================== IATA T5 FIELDS ===================== */

    String recordType;
    String spareByte2;

    String airlineDesignator;
    String releaseDateRaw;

    String spare13To187;

    String serialCheckReference;
    String continuationEndCode;

    String recordSerialNumber;

    String rawRecord;

    /* ===================== AUDIT ===================== */

    Instant parsedAt;


    /* ===================== MAPPER ===================== */

    public static SsimInboundTrailerDTO toDto(SsimInboundTrailer entity) {

        return SsimInboundTrailerDTO.builder()
                .id(entity.getId())
                .fileId(entity.getInboundFile().getId())

                .recordType(entity.getRecordType())
                .spareByte2(entity.getSpareByte2())

                .airlineDesignator(entity.getAirlineDesignator())
                .releaseDateRaw(entity.getReleaseDateRaw())

                .spare13To187(entity.getSpare13To187())

                .serialCheckReference(entity.getSerialCheckReference())
                .continuationEndCode(entity.getContinuationEndCode())

                .recordSerialNumber(entity.getRecordSerialNumber())

                .rawRecord(entity.getRawRecord())

                .parsedAt(entity.getParsedAt())

                .build();
    }
}
