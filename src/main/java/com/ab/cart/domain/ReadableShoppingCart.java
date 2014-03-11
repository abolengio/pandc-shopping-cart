package com.ab.cart.domain;

import org.joda.money.Money;

import java.util.Collection;

public interface ReadableShoppingCart {
    Collection<ExpandedCartItem> getItems();
    Money getSubTotal();
}
