package com.ab.cart.rest.resource;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ReadableShoppingCart;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.Collection;

public class ShoppingCartResource extends ResourceSupport {

    private ReadableShoppingCart readableShoppingCart;

    public ShoppingCartResource(ReadableShoppingCart readableShoppingCart) {
        this.readableShoppingCart = readableShoppingCart;
    }

    public Collection<ExpandedCartItem> getItems() {
        return readableShoppingCart.getItems();
    }

    public BigDecimal getSubTotal() {
        return readableShoppingCart.getSubTotal().getAmount();
    }


}
