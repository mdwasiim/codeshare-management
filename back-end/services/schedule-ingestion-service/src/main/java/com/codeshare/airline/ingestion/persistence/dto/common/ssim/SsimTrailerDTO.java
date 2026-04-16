package com.codeshare.airline.ingestion.persistence.dto.common.ssim;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.ingestion.domain.enums.RecordType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SsimTrailerDTO extends CSMAuditableDTO implements SSIMMessageItem {

    /* =======================================================
       IDENTITY
       ======================================================= */

    private UUID fileId;

    /* =======================================================
       SSIM RECORD TYPE 5 (T5) – 200 BYTES
       ======================================================= */

    private RecordType recordType;            // Byte 1
    private String spareByte2;            // Byte 2

    private String airlineDesignator;     // Bytes 3–5
    private String releaseDateRaw;        // Bytes 6–12

    private String spare13To187;          // Bytes 13–187

    private String serialCheckReference;  // Bytes 188–193
    private String continuationEndCode;   // Byte 194

    private String recordSerialNumber;    // Bytes 195–200

}