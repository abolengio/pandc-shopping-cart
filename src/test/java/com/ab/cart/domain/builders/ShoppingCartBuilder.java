package com.ab.cart.domain.builders;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ReadableShoppingCart;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ShoppingCartBuilder {

    public static final Money ZERO = Money.of(CurrencyUnit.EUR, 0);
    private Collection<ExpandedCartItem> items = Collections.emptyList();
    private Money subTotal = ZERO;

    public static ShoppingCartBuilder shoppingCart() {
        return new ShoppingCartBuilder();
    }

    public ReadableShoppingCart build(){
        return new BuiltShoppingCart();
    }

    public ShoppingCartBuilder with() {
        return this;
    }

    public ShoppingCartBuilder empty() {
        items = Collections.emptyList();
        subTotal = ZERO;
        return this;
    }

    public ShoppingCartBuilder withItems(ExpandedCartItem... items) {
        this.items = Arrays.asList(items);
        return this;
    }

    public ShoppingCartBuilder withSubTotal(Double subTotal) {
        this.subTotal = Money.of(CurrencyUnit.EUR, subTotal);
        return this;
    }

    class BuiltShoppingCart implements ReadableShoppingCart {

        @Override
        public Collection<ExpandedCartItem> getItems() {
            return items;
        }

        @Override
        public Money getSubTotal() {
            return subTotal;
        }
    }
}
