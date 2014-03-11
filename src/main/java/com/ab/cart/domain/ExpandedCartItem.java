package com.ab.cart.domain;

import org.joda.money.Money;

public class ExpandedCartItem extends CartItem {

    private final EffectivePriceProduct product;

    public ExpandedCartItem(EffectivePriceProduct product, int quantity) {
        super(product.getProductId(), quantity);
        this.product = product;
    }

    public EffectivePriceProduct getProduct() {
        return product;
    }

    public Money getSubTotal() {
        return product.getEffectivePrice().multipliedBy(getQuantity());
    }
}
