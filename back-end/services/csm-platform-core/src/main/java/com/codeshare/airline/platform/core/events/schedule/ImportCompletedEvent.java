package com.codeshare.airline.platform.core.events.schedule;

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
public class ImportCompletedEvent {
    private UUID importedScheduleId;
    private UUID importBatchId;
    private MessageType messageType;
    private String airlineCode;
    private String sourceName;
    private Instant completedAt;
}
