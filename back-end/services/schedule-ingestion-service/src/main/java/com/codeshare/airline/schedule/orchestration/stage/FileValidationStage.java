package com.codeshare.airline.schedule.orchestration.stage;

public interface FileValidationStage<TContext> {

    void execute(TContext context);
}