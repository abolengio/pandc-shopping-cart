package com.ab.cart.repository.impl;

import org.joda.time.DateTime;
import org.joda.time.ReadableInterval;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductRebateTimeFrameParserTest {

    @Test
    public void shouldParseTimeFrame() {
        ProductRebateTimeFrameParser parser = new ProductRebateTimeFrameParser();
        ReadableInterval interval = parser.parse("2014-02-28:15:00:00-2014-02-28:16:07:10");
        assertThat(interval.getStart().getMillis(), is(DateTime.parse("2014-02-28T15:00:00.000+01:00").getMillis()));
        assertThat(interval.getEnd().getMillis(), is(DateTime.parse("2014-02-28T16:07:10.000+01:00").getMillis()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenStartDateIsLaterThanEndDate() {
        ProductRebateTimeFrameParser parser = new ProductRebateTimeFrameParser();
        parser.parse("2014-02-29:12:00:00-2014-02-28:12:00:00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfInputIsShorterThanWhenStartDateIsLaterThanEndDate() {
        ProductRebateTimeFrameParser parser = new ProductRebateTimeFrameParser();
        parser.parse("2014-02-29:12:00:00-2014-02-28:12:00:00");
    }
}
