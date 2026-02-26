package com.codeshare.airline.schedule.parsing.ssim.dto;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundTrailer;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class Record5Trailer {

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

    public static Record5Trailer toDto(SsimInboundTrailer entity) {

        return Record5Trailer.builder()
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
