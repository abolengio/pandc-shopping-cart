package com.ab.cart.domain;

import org.joda.money.Money;
import org.joda.time.ReadableInterval;

public class EffectivePricingProduct extends Product {

    public EffectivePricingProduct(String productId, String name, Money originalPrice, ReadableInterval rebateTimeFrame) {   //todo use builder ?
        super( productId,  name,  originalPrice, rebateTimeFrame);
    }

    public Money getEffectivePrice() {
        return getPrice();  //todo implement
    }
}
