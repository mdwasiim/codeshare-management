package com.codeshare.airline.inbound.dto.common.ssim;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.inbound.domain.enums.RecordType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SsimCarrierDTO extends CSMAuditableDTO implements SSIMMessageItem {

    /* =======================================================
       IDENTIFIERS
       ======================================================= */

    private UUID fileId;

    /* =======================================================
       SSIM RECORD TYPE 2 (T2) – 200 BYTES
       ======================================================= */

    private RecordType recordType;                 // Byte 1
    private TimeMode timeMode;                 // Byte 2
    private String airlineCode;                // Bytes 3–5

    private String spare6To10;                 // Bytes 6–10
    private String season;                     // Bytes 11–13
    private String spare14;                    // Byte 14

    private String validityStartRaw;           // Bytes 15–21
    private String validityEndRaw;             // Bytes 22–28
    private String creationDateRaw;            // Bytes 29–35

    private String titleOfData;                // Bytes 36–64
    private String releaseDateRaw;             // Bytes 65–71

    private String scheduleStatus;             // Byte 72
    private String creatorReference;           // Bytes 73–107

    private String duplicateDesignatorMarker;  // Byte 108
    private String generalInformation;         // Bytes 109–169

    private String inflightServiceInfo;        // Bytes 170–188
    private String electronicTicketingInfo;    // Bytes 189–190

    private String creationTimeRaw;            // Bytes 191–194
    private String recordSerialNumber;         // Bytes 195–200

}
