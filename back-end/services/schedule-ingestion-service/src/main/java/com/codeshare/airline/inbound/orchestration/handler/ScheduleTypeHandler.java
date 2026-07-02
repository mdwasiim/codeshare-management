package com.codeshare.airline.inbound.orchestration.handler;

import com.codeshare.airline.inbound.domain.context.ErrorContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationMessage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface ScheduleTypeHandler<TParsed, TMetadata> {

    /**
     * Supported schedule type (ASM, SSM, SSIM)
     */
    MessageType supportedType();

    /**
     * Stream extractor (type-specific message splitting)
     */
    void streamExtractor(InputStream is, Consumer<List<String>> consumer);


    /**
     * Structural validation (before parsing)
     */
    ValidationResult validate(TParsed parsed, TMetadata metadata);

    /**
     * Parse message into domain object
     */
    TParsed parse(List<String> lines);

    /**
     * Persists valid parsed data into the system (single record).
     */
    void persist(TParsed parsed, TMetadata metadata);

    /**
     * Persists a batch of parsed records.
     * @param parsedList list of parsed domain objects
     * @param metadata   ingestion metadata
     */
    default void persistBatch(List<TParsed> parsedList, TMetadata metadata) {
        for (TParsed parsed : parsedList) {
            persist(parsed, metadata);
        }
    }
    /**
     * Persist validation or parsing errors
     */
    void persistError(
            ErrorContext context,
            ValidationStage stage,
            List<ValidationMessage> messages
    );

    // 🔥 ADD THIS
    default boolean isParallelSafe() {
        return false; // safe default
    }

    // 🔥 (optional but recommended)
    default boolean isBatchSupported() {
        return false;
    }
}