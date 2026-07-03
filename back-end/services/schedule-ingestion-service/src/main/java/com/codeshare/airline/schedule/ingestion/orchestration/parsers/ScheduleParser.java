package com.codeshare.airline.schedule.ingestion.orchestration.parsers;

import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;

import java.util.List;

public interface ScheduleParser<T> {

    ScheduleGroupedMessage groupMessage(List<String> lines);

    T parseMessage(ScheduleGroupedMessage grouped);
}