package com.ab.cart.domain.converters;

import com.ab.cart.domain.CartItem;
import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.EffectivePriceProductProvider;
import com.ab.cart.domain.ExpandedCartItem;
import com.google.common.base.Function;

public class CartItemToExpandedCartItemTransformer implements Function<CartItem, ExpandedCartItem> {

    private EffectivePriceProductProvider effectivePriceProductProvider;

    public CartItemToExpandedCartItemTransformer(EffectivePriceProductProvider effectivePriceProductProvider){
        this.effectivePriceProductProvider = effectivePriceProductProvider;
    }

    @Override
    public ExpandedCartItem apply(CartItem cartItem) {
        EffectivePriceProduct product = effectivePriceProductProvider.getProduct(cartItem.getProductId());
        return new ExpandedCartItem(product, cartItem.getQuantity());
    }
}
