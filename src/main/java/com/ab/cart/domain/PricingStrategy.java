package com.ab.cart.domain;

import org.joda.money.Money;

import java.math.RoundingMode;

public class PricingStrategy {

    public static Double rebateDiscount = 0.2;
    private Clock clock;

    public PricingStrategy(Clock clock) {
        //To change body of created methods use File | Settings | File Templates.
        this.clock = clock;
    }

    public Money getEffectivePrice(Product product) {
        Money effectivePrice = product.getPrice();
        if (product.getRebateTimeFrame() != null) {
            if (product.getRebateTimeFrame().contains(clock.getCurrentTime())) {
                effectivePrice = product.getPrice().multipliedBy(1 - rebateDiscount, RoundingMode.UP);
            }
        }
        return effectivePrice;
    }

}
