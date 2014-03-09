package com.ab.cart.domain;

import java.util.Collection;
import java.util.LinkedHashMap;

public class ShoppingCart {

    private LinkedHashMap<String, CartItem> items; //todo thread safety

    public ShoppingCart() {
        items = new LinkedHashMap<>();
    }

    public void addItem(String productId, int quantity) {
        if(items.containsKey(productId)) {
            items.put(productId, new CartItem(productId, quantity + items.get(productId).getQuantity()));
        } else
            items.put(productId, new CartItem(productId, quantity));    //todo redundancy
    }

    public CartItem getItem(String productId) {
        return items.get(productId);
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }
}
