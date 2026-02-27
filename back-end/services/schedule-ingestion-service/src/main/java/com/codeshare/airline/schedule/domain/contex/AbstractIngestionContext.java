package com.codeshare.airline.schedule.domain.contex;

import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class AbstractIngestionContext<TMetadata, TParsed> {

    /**
     * Original transport-level source file
     */
    protected ScheduleSourceFile sourceFile;

    /**
     * Persisted inbound metadata entity
     * (ScheduleInboundFile OR SsimParsedFile)
     */
    protected TMetadata inboundFile;

    /**
     * Structural validation result
     */
    protected ValidationResult structuralResult;

    /**
     * Business validation result
     */
    protected ValidationResult businessResult;

    /**
     * Parsed result (chapter-specific)
     */
    protected TParsed parsedResult;
}