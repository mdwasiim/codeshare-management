package com.codeshare.airline.schedule.ingestion.dto.ssim.record;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.schedule.ingestion.domain.enums.RecordType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SsimDataElementDTO extends CSMAuditableDTO {

    private UUID flightLegId;

    /* =======================================================
       1–14 HEADER
       ======================================================= */

    private RecordType recordType;                     // Byte 1
    private String operationalSuffix;              // Byte 2
    private String airlineCode;                    // Bytes 3–5
    private String flightNumber;                   // Bytes 6–9
    private String itineraryVariationIdentifier;   // Bytes 10–11
    private String legSequenceNumber;              // Bytes 12–13
    private String serviceType;                    // Byte 14

    /* =======================================================
       15–28 STRUCTURE
       ======================================================= */

    private String spare15To27;                    // Bytes 15–27
    private String itineraryVariationOverflow;     // Byte 28

    /* =======================================================
       29–39 SEGMENT IDENTIFIERS
       ======================================================= */

    private String boardPointIndicator;            // Byte 29
    private String offPointIndicator;              // Byte 30
    private String dataElementIdentifier;          // Bytes 31–33
    private String boardPoint;                     // Bytes 34–36
    private String offPoint;                       // Bytes 37–39

    /* =======================================================
       40–194 DATA PAYLOAD
       ======================================================= */

    private String deiData;                        // Bytes 40–194

    /* =======================================================
       195–200 FOOTER
       ======================================================= */

    private String recordSerialNumber;             // Bytes 195–200

}
