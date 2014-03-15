package com.ab.cart.rest.resource;

import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.domain.ReadableShoppingCart;
import com.ab.cart.rest.controller.UriFor;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.joda.money.Money;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

import static com.google.common.collect.Iterables.transform;

public class ShoppingCartResource extends BaseResource {

    private ReadableShoppingCart readableShoppingCart;

    private static Link selfLink = new Link(UriFor.cart, "self", RequestMethod.GET);
    private static Link addLink = new Link(UriFor.cartItems, "/shopping-cart/add-item", RequestMethod.POST);

    public ShoppingCartResource(ReadableShoppingCart readableShoppingCart) {
        super(selfLink, addLink);
        this.readableShoppingCart = readableShoppingCart;
    }

    public Collection<ShoppingCartItemResource> getItems() {
        return Lists.newArrayList(transform(readableShoppingCart.getItems(), new Function<ExpandedCartItem, ShoppingCartItemResource>(){
            @Override
            public ShoppingCartItemResource apply( ExpandedCartItem cartItem) {
                return new ShoppingCartItemResource(cartItem);
            }
        }));
    }

    public Money getSubTotal() {
        return readableShoppingCart.getSubTotal();
    }


}
