package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO {
    private UUID changeSetId;
    private UUID importedScheduleId;
    private UUID importBatchId;
    private MessageType messageType;
    private String airlineCode;
    private String partnerCode;
    private ApprovalMode acceptanceMode;
    private String messageReference;
    private String sourceName;
    private Instant createdAt;
    private String status;

    @Builder.Default
    private List<ScheduleFlightChangeDTO> flightChanges = new ArrayList<>();
}

