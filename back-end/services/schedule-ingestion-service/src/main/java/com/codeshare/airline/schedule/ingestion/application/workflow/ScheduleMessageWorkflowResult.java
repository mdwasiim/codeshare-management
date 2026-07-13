package com.codeshare.airline.schedule.ingestion.application.workflow;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleMessageWorkflowResult {

    private final AbstractIngestionContext<?, ?> errorContext;
    private final AbstractIngestionContext<?, ?> parsedContext;
    private final ValidationStage failureStage;
    private final List<ValidationMessage> messages;

    public boolean hasErrors() {
        return messages != null && messages.stream().anyMatch(ValidationMessage::isError);
    }
}
