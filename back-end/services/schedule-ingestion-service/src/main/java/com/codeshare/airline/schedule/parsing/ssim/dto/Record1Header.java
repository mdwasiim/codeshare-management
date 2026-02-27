package com.codeshare.airline.schedule.parsing.ssim.dto;

import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundHeader;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class Record1Header {

    UUID id;
    UUID fileId;

    String recordType;
    String titleOfContents;

    String spare36To40;
    String numberOfSeasons;
    String spare42To191;

    String datasetSerialNumber;
    String recordSerialNumber;

    String rawRecord;

    Instant parsedTimestamp;
    SsimParsedFile inboundFile;


    public static Record1Header toDto(SsimInboundHeader entity) {

        if (entity == null) return null;

        return Record1Header.builder()
                .id(entity.getId())
                .fileId(
                        entity.getInboundFile() != null
                                ? entity.getInboundFile().getId()
                                : null
                )

                .recordType(entity.getRecordType())
                .titleOfContents(entity.getTitleOfContents())

                .spare36To40(entity.getSpare36To40())
                .numberOfSeasons(entity.getNumberOfSeasons())
                .spare42To191(entity.getSpare42To191())

                .datasetSerialNumber(entity.getDatasetSerialNumber())
                .recordSerialNumber(entity.getRecordSerialNumber())

                .rawRecord(entity.getRawRecord())
                .parsedTimestamp(entity.getParsedTimestamp())

                .build();
    }


}
