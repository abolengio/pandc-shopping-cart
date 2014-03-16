package com.ab.cart.repository.impl;

import com.ab.cart.utils.Preferences;
import org.joda.time.Interval;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.google.common.base.Strings.isNullOrEmpty;

public class ProductRebateTimeFrameParser {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd:HH:mm:ss").withZone(Preferences.SYSTEM_TIME_ZONE);

    public ReadableInterval parse(String interval) {
        if(isNullOrEmpty(interval) || interval.length() != 39) {
            throw new IllegalArgumentException("Error parsing product rebate timeframe; " +
                                            "expected format is yyyy-MM-dd:HH:mm:ss-yyyy-MM-dd:HH:mm:ss");
        }
        String start = interval.substring(0, 19);
        String end = interval.substring(20);
        return new Interval(formatter.parseDateTime(start), formatter.parseDateTime(end));
    }
}
