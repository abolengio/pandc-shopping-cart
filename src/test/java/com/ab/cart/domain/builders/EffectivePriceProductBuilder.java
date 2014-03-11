package com.ab.cart.domain.builders;

import com.ab.cart.domain.EffectivePriceProduct;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EffectivePriceProductBuilder {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    String productId = null;
    String name = "Default name";
    Money price = Money.of(CurrencyUnit.EUR, 9.99);
    String rebateTimeFrameStart = null;
    String rebateTimeFrameEnd = null;

    public static EffectivePriceProductBuilder productWithId(String productId) {
        return new EffectivePriceProductBuilder(productId);
    }

    public EffectivePriceProductBuilder(String productId) {
        this.productId = productId;
    }

    public EffectivePriceProduct build() {
        Interval rebateTimeFrame;
        if(rebateTimeFrameStart == null && rebateTimeFrameEnd == null) {
            rebateTimeFrame = null;
        } else if (rebateTimeFrameStart != null && rebateTimeFrameEnd != null) {
            rebateTimeFrame = new Interval(formatter.parseDateTime(rebateTimeFrameStart), formatter.parseDateTime(rebateTimeFrameEnd));

        } else throw new IllegalArgumentException("Both start and end of rebate timeframe should be specified");
        return new EffectivePriceProduct(productId, name, price, rebateTimeFrame);

    }

    public EffectivePriceProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EffectivePriceProductBuilder price(Double price) {
        this.price = Money.of(CurrencyUnit.EUR, price);
        return this;
    }

    public RebateTimeFrameBuilder rebateTimeframe() {
        return new RebateTimeFrameBuilder(this);
    }

    public class RebateTimeFrameBuilder {

        private EffectivePriceProductBuilder productBuilder;

        public RebateTimeFrameBuilder(EffectivePriceProductBuilder productBuilder) {
            this.productBuilder = productBuilder;
        }

        public RebateTimeFrameBuilder start(String start) {
            rebateTimeFrameStart = start;
            return this;
        }

        public EffectivePriceProductBuilder end(String end) {
            rebateTimeFrameEnd = end;
            return productBuilder;
        }

    }
}
