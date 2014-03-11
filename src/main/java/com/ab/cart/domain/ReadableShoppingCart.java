package com.ab.cart.domain;

import org.joda.money.Money;

import java.util.List;

public interface ReadableShoppingCart {
    List<ExpandedCartItem> getItems();
    Money getSubTotal();
}
