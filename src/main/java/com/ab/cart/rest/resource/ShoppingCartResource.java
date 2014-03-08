package com.ab.cart.rest.resource;

import com.ab.cart.domain.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartResource {

    private List<CartItem> items;
    private BigDecimal total = new BigDecimal("0");

    public ShoppingCartResource(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }


}
