package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportBatchDTO {
    private UUID importBatchId;
    private UUID importedScheduleId;
    private MessageType messageType;
    private String airlineCode;
    private String sourceName;
    private Instant completedAt;
    private ImportedScheduleDTO importedSchedule;
}
