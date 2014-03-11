package com.ab.cart.domain;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpandedCartItemTest {

    @Mock
    EffectivePriceProduct product;

    @Test
    public void shouldCalculateSubTotalForOneItem() {
        when(product.getEffectivePrice()).thenReturn(Money.of(CurrencyUnit.EUR, 19.48));
        ExpandedCartItem item = new ExpandedCartItem(product, 1);
        assertThat(item.getSubTotal(), is(Money.of(CurrencyUnit.EUR, 19.48)));
    }

    @Test
    public void shouldCalculateSubTotalForQuantityMoreThanOne() {
        when(product.getEffectivePrice()).thenReturn(Money.of(CurrencyUnit.EUR, 10.0));
        ExpandedCartItem item = new ExpandedCartItem(product, 10);
        assertThat(item.getSubTotal(), is(Money.of(CurrencyUnit.EUR, 100.00)));
    }


}
