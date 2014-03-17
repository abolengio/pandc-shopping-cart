package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.CartItem;
import com.ab.cart.repository.ShoppingCartItemsRepository;

import java.util.List;

/**
 * simple implementation of ShoppingCartRepository which recreates the whole shopping cart each time from the underlying source
 * (which may or may not be efficient depending on underlying source)
 */
public class EventSourcingShoppingCartItemsRepository implements ShoppingCartItemsRepository {

    private AggregatingShoppingCartFactory aggregatingShoppingCartFactory;
    private final ShoppingCartEventSource shoppingCartEventSource;

    public EventSourcingShoppingCartItemsRepository(AggregatingShoppingCartFactory aggregatingShoppingCartFactory, ShoppingCartEventSource shoppingCartEventSource) {
        this.aggregatingShoppingCartFactory = aggregatingShoppingCartFactory;
        this.shoppingCartEventSource = shoppingCartEventSource;
    }

    @Override
    public List<CartItem> getShoppingCartItems() {
        AggregatingShoppingCart aggregatingShoppingCart = aggregatingShoppingCartFactory.create();
        shoppingCartEventSource.readInto(aggregatingShoppingCart);
        return aggregatingShoppingCart.getItems();
    }
}
