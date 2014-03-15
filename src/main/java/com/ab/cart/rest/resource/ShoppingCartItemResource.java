package com.ab.cart.rest.resource;

import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.ExpandedCartItem;
import com.ab.cart.rest.controller.UriFor;
import org.joda.money.Money;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.util.StringUtils.replace;

public class ShoppingCartItemResource extends BaseResource {

    private ExpandedCartItem cartItem;

    private static Link removeLink(String productId) {
        return new Link(uriForCartItemWithProductId(productId), "/shopping-cart/remove-item", RequestMethod.DELETE);
    }

    private static Link updateQuantityLink(String productId) {
        return new Link(uriForCartItemWithProductId(productId), "/shopping-cart/update-quantity", RequestMethod.PUT);
    }

    public ShoppingCartItemResource(ExpandedCartItem cartItem) {
        super(removeLink(cartItem.getProductId()), updateQuantityLink(cartItem.getProductId()));
        this.cartItem = cartItem;
    }

    private static String uriForCartItemWithProductId(String productId) {
        return replace(UriFor.cartItem, "{productId}" , productId);
    }

    public EffectivePriceProduct getProduct() {
        return cartItem.getProduct();
    }

    public Money getSubTotal() {
        return cartItem.getSubTotal();
    }

    public String getProductId() {
        return cartItem.getProductId();
    }

    public int getQuantity() {
        return cartItem.getQuantity();
    }
}
