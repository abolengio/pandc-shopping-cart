package com.ab.cart.domain;

public interface WritableShoppingCart {
    void add(String productId, int quantity);
    void remove(String productId);
    void updateQuantity(String productId, int quantity);
}
