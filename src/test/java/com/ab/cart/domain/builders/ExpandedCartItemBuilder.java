package com.ab.cart.domain.builders;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.Product;

public class ExpandedCartItemBuilder {

    private Product product = null;
    private int quantity = 1;

    public static ExpandedCartItemBuilder cartItem() {
        return new ExpandedCartItemBuilder();
    }

    public ExpandedCartItemBuilder with(Product product) {
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
