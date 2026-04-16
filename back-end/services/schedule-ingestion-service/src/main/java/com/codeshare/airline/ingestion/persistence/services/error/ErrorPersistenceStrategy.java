package com.codeshare.airline.ingestion.persistence.services.error;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.validations.model.ValidationMessage;

import java.util.List;

public interface ErrorPersistenceStrategy {

    MessageType getSupportedType();

    void persist(AbstractIngestionContext<?, ?> context,
                 ValidationStage stage,
                 List<ValidationMessage> messages);
}