package com.codeshare.airline.ingestion.orchestration.parsers;

import com.codeshare.airline.ingestion.domain.context.ScheduleGroupedMessage;

import java.util.List;

public interface ScheduleParser<T> {

    ScheduleGroupedMessage groupMessage(List<String> lines);

    T parseMessage(ScheduleGroupedMessage grouped);
}