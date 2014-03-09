package com.ab.cart.domain;

import org.joda.money.Money;

public class Product {

    private final String productId;
    private final String name;
    private final Money price;

    Product(String productId, String name, Money price) {

        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
