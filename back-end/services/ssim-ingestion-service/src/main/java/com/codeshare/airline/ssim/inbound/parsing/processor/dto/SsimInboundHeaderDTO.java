package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundHeader;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SsimInboundHeaderDTO {

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
    SsimInboundFileDTO inboundFile;


    public static SsimInboundHeaderDTO toDto(SsimInboundHeader entity) {

        if (entity == null) return null;

        return SsimInboundHeaderDTO.builder()
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
