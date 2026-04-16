package com.codeshare.airline.ingestion.persistence.services.error;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.validations.model.ValidationMessage;

import java.util.List;

public interface ErrorPersistenceService {

    void persist(AbstractIngestionContext<?, ?> context, ValidationStage stage, List<ValidationMessage> messages);
}