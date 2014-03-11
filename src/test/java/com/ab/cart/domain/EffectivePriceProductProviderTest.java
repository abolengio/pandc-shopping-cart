package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
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

    @Test
    public void shouldSetEffectivePriceToOriginalPriceWhenThereIsNoRebate() {
        //given
        Money originalPrice = Money.of(CurrencyUnit.EUR, 83.9);
        Product product = new Product("some-product-id", "some description", originalPrice);
        when(productCatalogue.getProduct("some-product-id")).thenReturn(product);
        //when
        EffectivePriceProductProvider pricingProvider = new EffectivePriceProductProvider(productCatalogue);
        EffectivePriceProduct effectivePriceProduct = pricingProvider.getProduct("some-product-id");
        //then
        assertThat(effectivePriceProduct.getEffectivePrice(), is(equalTo(originalPrice)));
        assertThat(effectivePriceProduct.getName(), is("some description"));
        assertThat(effectivePriceProduct.getProductId(), is("some-product-id"));
        assertThat(effectivePriceProduct.getPrice(), is(originalPrice));
        assertThat(effectivePriceProduct.getRebateTimeFrame(), is(nullValue()));
    }

    @Test
    public void shouldSetEffectivePriceToDiscountedPriceWhenThereIsRebateAndTheTimeIsRight() {
        //given
        Money originalPrice = Money.of(CurrencyUnit.EUR, 100.0);
        Product product = new Product("some-product-id", "some description", originalPrice,
                                        timeFrame().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build());
        when(productCatalogue.getProduct("some-product-id")).thenReturn(product);
        //when
        EffectivePriceProductProvider pricingProvider = new EffectivePriceProductProvider(productCatalogue);
        EffectivePriceProduct effectivePriceProduct = pricingProvider.getProduct("some-product-id");
        //then
        assertThat(effectivePriceProduct.getEffectivePrice(), is(Money.of(CurrencyUnit.EUR, 80.0)));
        assertThat(effectivePriceProduct.getName(), is("some description"));
        assertThat(effectivePriceProduct.getProductId(), is("some-product-id"));
        assertThat(effectivePriceProduct.getPrice(), is(originalPrice));
        assertThat(effectivePriceProduct.getRebateTimeFrame(), is(nullValue()));
    }
}
