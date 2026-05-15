package com.codeshare.airline.inbound.dto.common.ssim;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.inbound.domain.enums.RecordType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SsimHeaderDTO extends CSMAuditableDTO implements SSIMMessageItem {

    /* =======================================================
       IDENTITY
       ======================================================= */

    private UUID fileId;

    /* =======================================================
       SSIM RECORD TYPE 1 (T1) – 200 BYTES
       ======================================================= */

    private RecordType recordType;            // Byte 1

    private String titleOfContents;       // Bytes 2–35

    private String spare36To40;            // Bytes 36–40

    private Integer numberOfSeasons;       // Byte 41

    private String spare42To191;           // Bytes 42–191

    private String datasetSerialNumber;    // Bytes 192–194

    private String recordSerialNumber;     // Bytes 195–200
}
