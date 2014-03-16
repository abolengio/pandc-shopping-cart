package com.ab.cart.domain;

import com.ab.cart.utils.Preferences;
import org.joda.time.DateTime;

public class Clock {

    public DateTime getCurrentTime() {
        return new DateTime(Preferences.SYSTEM_TIME_ZONE);
    }
}
