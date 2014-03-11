package com.ab.cart.domain.builders;

import org.joda.time.MutableInterval;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeFrameBuilder {
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    private MutableInterval interval = new MutableInterval();

    public TimeFrameBuilder() {
    }

    public static TimeFrameBuilder timeFrame() {
        return new TimeFrameBuilder();
    }

    public TimeFrameBuilder start(String start) {
        interval.setStart(formatter.parseDateTime(start));
        return this;
    }

    public TimeFrameBuilder end(String end) {
        interval.setEnd(formatter.parseDateTime(end));
        return this;
    }

    public ReadableInterval build() {
        return interval;
    }
}
