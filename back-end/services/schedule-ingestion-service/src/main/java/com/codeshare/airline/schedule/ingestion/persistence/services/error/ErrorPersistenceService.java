package com.codeshare.airline.schedule.ingestion.persistence.services.error;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;

import java.util.List;

public interface ErrorPersistenceService {

    void persist(AbstractIngestionContext<?, ?> context, ValidationStage stage, List<ValidationMessage> messages);
}