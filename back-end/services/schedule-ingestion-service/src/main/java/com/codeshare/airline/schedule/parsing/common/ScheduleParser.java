package com.codeshare.airline.schedule.parsing.common;

import java.io.InputStream;

public interface ScheduleParser<T> {

    T parse(InputStream stream);

}