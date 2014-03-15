package com.ab.cart.rest.json;

import com.ab.cart.domain.EffectivePriceProduct;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.money.Money;
import org.joda.time.ReadableInterval;

public class ShoppingCartModule extends SimpleModule {

    public ShoppingCartModule() {
        addSerializer(Money.class, new MoneySerializer());
        addSerializer(EffectivePriceProduct.class, new EffectivePriceProductSerializer());
        addSerializer(ReadableInterval.class, new TimeIntervalSerializer());
    }

}
