package com.ab.cart.repository.impl.eventsourced;

public class AggregatingShoppingCartFactory {

    public AggregatingShoppingCart create() {
        return new AggregatingShoppingCart();
    }
}
