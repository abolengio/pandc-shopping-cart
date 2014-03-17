package com.ab.cart.domain.builders;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ReadableShoppingCart;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadableShoppingCartBuilder {

    public static final Money ZERO = Money.of(CurrencyUnit.EUR, 0);
    private List<ExpandedCartItem> items = Collections.emptyList();
    private Money subTotal = ZERO;

    public static ReadableShoppingCartBuilder shoppingCart() {
        return new ReadableShoppingCartBuilder();
    }

    public ReadableShoppingCart build(){
        return new BuiltShoppingCart();
    }

    public ReadableShoppingCartBuilder with() {
        return this;
    }

    public ReadableShoppingCartBuilder empty() {
        items = Collections.emptyList();
        subTotal = ZERO;
        return this;
    }

    public ReadableShoppingCartBuilder withItems(ExpandedCartItem... items) {
        this.items = Arrays.asList(items);
        return this;
    }

    public ReadableShoppingCartBuilder withSubTotal(Double subTotal) {
        this.subTotal = Money.of(CurrencyUnit.EUR, subTotal);
        return this;
    }

    /**
     * Sets the items and calculates cart subTotal
     * @param expandedCartItems
     * @return
     */
    public ReadableShoppingCartBuilder withItems(Iterable<ExpandedCartItem> expandedCartItems) {    //todo move into shopping cart
        items = new ArrayList<>();
        Money cartSubTotal = ZERO;
        for(ExpandedCartItem item : expandedCartItems) {
            items.add(item);
            cartSubTotal = cartSubTotal.plus(item.getSubTotal());
        }
        this.subTotal = cartSubTotal;
        return this;
    }

    class BuiltShoppingCart implements ReadableShoppingCart {

        @Override
        public List<ExpandedCartItem> getItems() {
            return items;
        }

        @Override
        public Money getSubTotal() {
            return subTotal;
        }
    }
}
