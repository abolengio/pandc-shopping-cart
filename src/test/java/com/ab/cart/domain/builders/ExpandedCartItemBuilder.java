package com.ab.cart.domain.builders;

import com.ab.cart.domain.EffectivePricingProduct;
import com.ab.cart.domain.ExpandedCartItem;

public class ExpandedCartItemBuilder {

    private EffectivePricingProduct product = null;
    private int quantity = 1;

    public static ExpandedCartItemBuilder cartItem() {
        return new ExpandedCartItemBuilder();
    }

    public ExpandedCartItemBuilder with(EffectivePricingProduct product) {
        this.product = product;
        return this;
    }

    public ExpandedCartItemBuilder quantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ExpandedCartItem build() {
        return new ExpandedCartItem(product, quantity);
    }
}
