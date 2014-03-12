package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.CartItem;
import com.ab.cart.domain.WritableShoppingCart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class AggregatingShoppingCart implements WritableShoppingCart {

    private LinkedHashMap<String, CartItem> items;

    AggregatingShoppingCart() {
        items = new LinkedHashMap<>();
    }

    @Override
    public void add(String productId, int quantity) {
        if(items.containsKey(productId)) {
            items.put(productId, new CartItem(productId, quantity + items.get(productId).getQuantity()));
        } else
            items.put(productId, new CartItem(productId, quantity));
    }

    @Override
    public void remove(String productId) {
        items.remove(productId);
    }

    @Override
    public void updateQuantity(String productId, int quantity) {
        if(quantity < 1) {
            remove(productId);
        } else {
            items.put(productId, new CartItem(productId, quantity));
        }
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

}
