package com.ab.cart.domain.converters;

import com.ab.cart.domain.CartItem;
import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.EffectivePriceProductProvider;
import com.ab.cart.domain.ExpandedCartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemToExpandedCartItemTransformerTest {

    @Mock
    EffectivePriceProductProvider effectivePriceProductProvider;

    @Test
    public void shouldEnhanceItemWithProductFromCatalogue() {
        EffectivePriceProduct effectivePriceProduct = mock(EffectivePriceProduct.class);
        when(effectivePriceProductProvider.getProduct("product-id-1")).thenReturn(effectivePriceProduct);
        when(effectivePriceProduct.getProductId()).thenReturn("product-id-1");
        CartItemToExpandedCartItemTransformer transformer = new CartItemToExpandedCartItemTransformer(effectivePriceProductProvider);
        ExpandedCartItem expandedCartItem = transformer.apply(new CartItem("product-id-1", 3));
        assertThat(expandedCartItem.getProductId(), is("product-id-1"));
        assertThat(expandedCartItem.getQuantity(), is(3));
        assertThat(expandedCartItem.getProduct(), is(effectivePriceProduct));
    }
}
