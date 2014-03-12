package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;

public interface ShoppingCartEventSource {
    public void readInto(WritableShoppingCart writableShoppingCart);
}
