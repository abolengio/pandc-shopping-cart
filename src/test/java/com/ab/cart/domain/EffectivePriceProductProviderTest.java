package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.ReadableInterval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ab.cart.domain.builders.TimeFrameBuilder.timeFrame;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EffectivePriceProductProviderTest {

    @Mock
    ProductCatalogue productCatalogue;

    @Mock
    PricingStrategy pricingStrategy;

    @Test
    public void shouldCopyProductDetailsFromProductAndSetEffectivePrice() {
        //given
        Product product = new Product("some-product-id", "some description", Money.of(CurrencyUnit.EUR, 83.9));
        when(productCatalogue.getProduct("some-product-id")).thenReturn(product);
        when(pricingStrategy.getEffectivePrice(product)).thenReturn(Money.of(CurrencyUnit.EUR, 293.83));
        //when
        EffectivePriceProductProvider pricingProvider = new EffectivePriceProductProvider(productCatalogue, pricingStrategy);
        EffectivePriceProduct effectivePriceProduct = pricingProvider.getProduct("some-product-id");
        //then
        assertThat(effectivePriceProduct.getName(), is("some description"));
        assertThat(effectivePriceProduct.getProductId(), is("some-product-id"));
        assertThat(effectivePriceProduct.getPrice(), is(Money.of(CurrencyUnit.EUR, 83.9)));
        assertThat(effectivePriceProduct.getRebateTimeFrame(), is(nullValue()));
        assertThat(effectivePriceProduct.getEffectivePrice(), is(Money.of(CurrencyUnit.EUR, 293.83)));
    }

    @Test
    public void shouldCopyRebateTimeFrameWhenProductHasOne() {
        //given
        ReadableInterval rebateTimeFrame = timeFrame().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        Product product = new Product("some-product-id", "some description", Money.of(CurrencyUnit.EUR, 100.0), rebateTimeFrame);
        when(productCatalogue.getProduct("some-product-id")).thenReturn(product);
        when(pricingStrategy.getEffectivePrice(product)).thenReturn(Money.of(CurrencyUnit.EUR, 293.83));
        //when
        EffectivePriceProductProvider pricingProvider = new EffectivePriceProductProvider(productCatalogue, pricingStrategy);
        EffectivePriceProduct effectivePriceProduct = pricingProvider.getProduct("some-product-id");
        //then
        assertThat(effectivePriceProduct.getRebateTimeFrame(), is(equalTo(rebateTimeFrame)));
    }

    @Test(expected = ProductDoesNotExistException.class)
    public void shouldThrowExceptionWhenProductDoesNotExist() {
        //given
        when(productCatalogue.getProduct("some-product-id")).thenReturn(null);
        //when
        EffectivePriceProductProvider pricingProvider = new EffectivePriceProductProvider(productCatalogue, pricingStrategy);
        pricingProvider.getProduct("some-product-id");
    }
}
