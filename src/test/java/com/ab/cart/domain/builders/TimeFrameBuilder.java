package com.ab.cart.domain.builders;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static java.lang.String.format;

public class TimeFrameBuilder {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DateTime start = null;
    private DateTime end = null;

    public TimeFrameBuilder() {
    }

    public static TimeFrameBuilder timeFrame() {
        return new TimeFrameBuilder();
    }

    public TimeFrameBuilder start(String start) {
        this.start = formatter.parseDateTime(start);
        return this;
    }

    public TimeFrameBuilder end(String end) {
        this.end = formatter.parseDateTime(end);
        return this;
    }

    public ReadableInterval build() {
        if(start == null || end == null) {
            throw new IllegalArgumentException(format("Error creating interval. Both start and end must be specified when" +
                                                    " creating Interval. What's been provided: start=%s end=%s",start, end));
        }
        return new Interval(start, end);
    }
}
