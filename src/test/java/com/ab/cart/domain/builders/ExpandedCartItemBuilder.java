package com.ab.cart.domain.builders;

import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.ExpandedCartItem;

public class ExpandedCartItemBuilder {

    private EffectivePriceProduct product = null;
    private int quantity = 1;

    public static ExpandedCartItemBuilder cartItem() {
        return new ExpandedCartItemBuilder();
    }

    public ExpandedCartItemBuilder with(EffectivePriceProduct product) {
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
