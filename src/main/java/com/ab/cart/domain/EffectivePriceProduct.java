package com.ab.cart.domain;

import org.joda.money.Money;
import org.joda.time.ReadableInterval;

public class EffectivePriceProduct extends Product {

    private final Money effectivePrice;

    public EffectivePriceProduct(String productId, String name, Money originalPrice, ReadableInterval rebateTimeFrame, Money effectivePrice) {   //todo use builder ?
        super( productId,  name,  originalPrice, rebateTimeFrame);
        this.effectivePrice = effectivePrice;
    }

    public Money getEffectivePrice() {
        return effectivePrice;
    }
}
