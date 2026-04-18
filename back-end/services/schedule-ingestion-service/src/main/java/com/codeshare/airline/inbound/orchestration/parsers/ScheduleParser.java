package com.codeshare.airline.inbound.orchestration.parsers;

import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;

import java.util.List;

public interface ScheduleParser<T> {

    ScheduleGroupedMessage groupMessage(List<String> lines);

    T parseMessage(ScheduleGroupedMessage grouped);
}