package com.codeshare.airline.inbound.services.error;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;

import java.util.List;

public interface ErrorPersistenceService {

    void persist(AbstractIngestionContext<?, ?> context, ValidationStage stage, List<ValidationMessage> messages);
}