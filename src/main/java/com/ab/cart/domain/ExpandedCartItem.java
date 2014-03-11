package com.ab.cart.domain;

import org.joda.money.Money;

public class ExpandedCartItem extends CartItem {

    private final EffectivePricingProduct product;

    public ExpandedCartItem(EffectivePricingProduct product, int quantity) {
        super(product.getProductId(), quantity);
        this.product = product;
    }

    public EffectivePricingProduct getProduct() {
        return product;
    }

    public Money getSubTotal() {
        return product.getEffectivePrice().multipliedBy(getQuantity());
    }
}
