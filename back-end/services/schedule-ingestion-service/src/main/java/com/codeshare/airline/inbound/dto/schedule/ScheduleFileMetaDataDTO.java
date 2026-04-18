package com.codeshare.airline.inbound.dto.schedule;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.ScheduleProfile;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ScheduleFileMetaDataDTO extends CSMAuditableDTO {

    private UUID fileId;
    private UUID loadId;
    private String fileName;

    private MessageType messageType;
    private SourceType sourceType;
    private ScheduleProfile scheduleProfile;

    private ProcessingStatus processingStatus;

    private TimeMode timeMode;

    private String airlineCode;

    private Long fileSizeBytes;
    private String checksum;

    private Instant receivedAt;
    private Instant processedAt;
    private Instant failedTimestamp;

    private String errorCode;

    private String sequenceReference;
    private String creatorReference;

    private Long version;

}