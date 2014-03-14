package com.ab.cart.rest.controller;

public class UriFor {

    public static final String versionPrefix = "/v1";

    public static final String cart = versionPrefix + "/cart";
    public static final String cartItems = versionPrefix + "/cart/items";
    public static final String cartItem = versionPrefix + "/cart/items/{productId}";
}
