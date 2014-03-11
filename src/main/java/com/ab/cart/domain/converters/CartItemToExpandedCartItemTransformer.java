package com.ab.cart.domain.converters;

import com.ab.cart.domain.CartItem;
import com.ab.cart.domain.EffectivePricingProduct;
import com.ab.cart.domain.EffectivePricingProvider;
import com.ab.cart.domain.ExpandedCartItem;
import com.google.common.base.Function;

public class CartItemToExpandedCartItemTransformer implements Function<CartItem, ExpandedCartItem> {

    private EffectivePricingProvider effectivePricingProvider;

    public CartItemToExpandedCartItemTransformer(EffectivePricingProvider effectivePricingProvider){
        this.effectivePricingProvider = effectivePricingProvider;
    }

    @Override
    public ExpandedCartItem apply(CartItem cartItem) {
        EffectivePricingProduct product = effectivePricingProvider.getProduct(cartItem.getProductId());
        return new ExpandedCartItem(product, cartItem.getQuantity());
    }
}
