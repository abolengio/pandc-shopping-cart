package com.ab.cart.domain;

public class CartItem {

    private String productId;
    private int quantity;

    CartItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getProductId() {
        return productId;
    }
}
