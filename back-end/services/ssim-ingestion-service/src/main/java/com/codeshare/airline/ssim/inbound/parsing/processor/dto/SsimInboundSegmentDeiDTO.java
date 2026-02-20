package com.codeshare.airline.ssim.inbound.parsing.processor.dto;

import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFlightLeg;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundSegmentDei;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SsimInboundSegmentDeiDTO {

    UUID id;
    UUID flightLegId;

    // 1–14 HEADER
    String recordType;
    String operationalSuffix;
    String airlineCode;
    String flightNumber;
    String itineraryVariationIdentifier;
    String legSequenceNumber;
    String serviceType;

    // 15–28 STRUCTURE
    String spare15To27;
    String itineraryVariationOverflow;

    // 29–39 SEGMENT IDENTIFIERS
    String boardPointIndicator;
    String offPointIndicator;
    String dataElementIdentifier;
    String boardPoint;
    String offPoint;

    // 40–194
    String deiData;

    // 195–200
    String recordSerialNumber;

    String rawRecord;
    Instant parsedTimestamp;

    SsimInboundFileDTO inboundFile;

    public static SsimInboundSegmentDeiDTO toDto(SsimInboundSegmentDei entity) {

        if (entity == null) return null;

        return SsimInboundSegmentDeiDTO.builder()
                .id(entity.getId())
                .flightLegId(
                        entity.getFlightLeg() != null
                                ? entity.getFlightLeg().getId()
                                : null
                )

                .recordType(entity.getRecordType())
                .operationalSuffix(entity.getOperationalSuffix())
                .airlineCode(entity.getAirlineCode())
                .flightNumber(entity.getFlightNumber())
                .itineraryVariationIdentifier(entity.getItineraryVariationIdentifier())
                .legSequenceNumber(entity.getLegSequenceNumber())
                .serviceType(entity.getServiceType())

                .spare15To27(entity.getSpare15To27())
                .itineraryVariationOverflow(entity.getItineraryVariationOverflow())

                .boardPointIndicator(entity.getBoardPointIndicator())
                .offPointIndicator(entity.getOffPointIndicator())
                .dataElementIdentifier(entity.getDataElementIdentifier())
                .boardPoint(entity.getBoardPoint())
                .offPoint(entity.getOffPoint())

                .deiData(entity.getDeiData())

                .recordSerialNumber(entity.getRecordSerialNumber())

                .rawRecord(entity.getRawRecord())
                .parsedTimestamp(entity.getParsedTimestamp())

                .build();
    }


    public static SsimInboundSegmentDei toEntity(
            SsimInboundSegmentDeiDTO dto,
            SsimInboundFlightLeg parentFlightLeg
    ) {

        if (dto == null) return null;

        SsimInboundSegmentDei entity = new SsimInboundSegmentDei();

        entity.setId(dto.getId());
        entity.setFlightLeg(parentFlightLeg);

        entity.setRecordType(dto.getRecordType());
        entity.setOperationalSuffix(dto.getOperationalSuffix());
        entity.setAirlineCode(dto.getAirlineCode());
        entity.setFlightNumber(dto.getFlightNumber());
        entity.setItineraryVariationIdentifier(dto.getItineraryVariationIdentifier());
        entity.setLegSequenceNumber(dto.getLegSequenceNumber());
        entity.setServiceType(dto.getServiceType());

        entity.setSpare15To27(dto.getSpare15To27());
        entity.setItineraryVariationOverflow(dto.getItineraryVariationOverflow());

        entity.setBoardPointIndicator(dto.getBoardPointIndicator());
        entity.setOffPointIndicator(dto.getOffPointIndicator());
        entity.setDataElementIdentifier(dto.getDataElementIdentifier());
        entity.setBoardPoint(dto.getBoardPoint());
        entity.setOffPoint(dto.getOffPoint());

        entity.setDeiData(dto.getDeiData());

        entity.setRecordSerialNumber(dto.getRecordSerialNumber());

        entity.setRawRecord(dto.getRawRecord());
        entity.setParsedTimestamp(dto.getParsedTimestamp());

        return entity;
    }

}
