package com.ab.cart.rest.controller;

public class CartItemParameter {

    private String productId;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
