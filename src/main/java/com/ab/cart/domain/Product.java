package com.ab.cart.domain;

import org.joda.money.Money;
import org.joda.time.ReadableInterval;

public class Product {

    private final String productId;
    private final String name;
    private final Money price;
    private final ReadableInterval rebateTimeFrame;

    public Product(String productId, String name, Money price) {

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.rebateTimeFrame = null;
    }

    public Product(String productId, String name, Money price, ReadableInterval rebateTimeFrame) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.rebateTimeFrame = rebateTimeFrame;
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

    public ReadableInterval getRebateTimeFrame() {
        return rebateTimeFrame;
    }
}
