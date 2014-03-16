package com.ab.cart.domain;

import com.ab.cart.utils.Preferences;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ab.cart.domain.builders.TimeFrameBuilder.timeFrame;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PricingStrategyTest {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(Preferences.SYSTEM_TIME_ZONE);

    @Mock
    private Clock clock;

    @Test
    public void shouldReturnNormalPriceIfThereIsNoRebateOnProduct() {
        PricingStrategy pricingStrategy = new PricingStrategy(clock);
        Money originalPrice = Money.of(CurrencyUnit.EUR, 83.9);
        Product product = new Product("some-product-id", "some description", originalPrice);
        Money effectivePrice = pricingStrategy.getEffectivePrice(product);
        assertThat(effectivePrice, is(equalTo(originalPrice))) ;
    }

    @Test
    public void shouldReturn20PercentDiscountWhenTheCurrentTimeIsWithinTheRebateTimeFrame() {
        when(clock.getCurrentTime()).thenReturn(formatter.parseDateTime("2014-04-15T18:00:00"));
        PricingStrategy pricingStrategy = new PricingStrategy(clock);
        ReadableInterval rebateTimeFrame = timeFrame().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        Product product = new Product("some-product-id", "some description", Money.of(CurrencyUnit.EUR, 200), rebateTimeFrame);
        Money effectivePrice = pricingStrategy.getEffectivePrice(product);
        assertThat(effectivePrice, is(equalTo(Money.of(CurrencyUnit.EUR, 160)))) ;
    }

    @Test
    public void shouldRoundThePriceUp() {
        PricingStrategy pricingStrategy = new PricingStrategy(clock);
        ReadableInterval rebateTimeFrame = timeFrame().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        when(clock.getCurrentTime()).thenReturn(formatter.parseDateTime("2014-04-15T18:00:00"));
        Product product1 = new Product("product-id1", "some description", Money.of(CurrencyUnit.EUR, 11.11), rebateTimeFrame);
        Product product2 = new Product("product-id2", "some description", Money.of(CurrencyUnit.EUR, 33.33), rebateTimeFrame);
        assertThat(pricingStrategy.getEffectivePrice(product1), is(equalTo(Money.of(CurrencyUnit.EUR, 8.89)))) ; //8.888
        assertThat(pricingStrategy.getEffectivePrice(product2), is(equalTo(Money.of(CurrencyUnit.EUR, 26.67)))) ; //26.664
    }

    @Test
    public void shouldReturnOriginalPriceWhenTheCurrentTimeIsOutsideOfTheRebateTimeFrame() {
        PricingStrategy pricingStrategy = new PricingStrategy(clock);
        ReadableInterval rebateTimeFrame = timeFrame().start("2014-04-01T12:37:00").end("2014-04-02T12:37:00").build();
        when(clock.getCurrentTime()).thenReturn(formatter.parseDateTime("2014-04-03T18:00:00"));
        Product product = new Product("some-product-id", "some description", Money.of(CurrencyUnit.EUR, 200), rebateTimeFrame);
        Money effectivePrice = pricingStrategy.getEffectivePrice(product);
        assertThat(effectivePrice, is(equalTo(Money.of(CurrencyUnit.EUR, 200)))) ;
    }

}
