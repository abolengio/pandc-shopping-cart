package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.google.common.base.Function;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Collection;
import java.util.LinkedHashMap;

import static com.google.common.collect.Iterables.transform;

//todo shopping cart doing too much
public class ShoppingCart {

    private LinkedHashMap<String, CartItem> items; //todo thread safety ?
    private ProductCatalogue productCatalogue;

    public ShoppingCart(ProductCatalogue productCatalogue) {
        this.productCatalogue = productCatalogue;
        items = new LinkedHashMap<>();
    }

    public void addItem(String productId, int quantity) {
        if (productCatalogue.getProduct(productId) == null) {
            throw new ProductDoesNotExistException(productId);   //todo why not in ProductCatalogue itself?
        }
        if(items.containsKey(productId)) {
            items.put(productId, new CartItem(productId, quantity + items.get(productId).getQuantity()));
        } else
            items.put(productId, new CartItem(productId, quantity));    //todo redundancy ?
    }

    public CartItem getItem(String productId) {
        return items.get(productId);
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }


    public Money getSubTotal() {
        Iterable<ExpandedCartItem> expandedCartItems = transform(items.values(), new Function<CartItem, ExpandedCartItem>() {
            @Override
            public ExpandedCartItem apply(CartItem cartItem) {
                return new ExpandedCartItem(productCatalogue.getProduct(cartItem.getProductId()), cartItem.getQuantity());
            }
        });

        Money subTotal = Money.zero(CurrencyUnit.EUR);
        for(ExpandedCartItem item : expandedCartItems) {
            subTotal = subTotal.plus(item.getProduct().getPrice().multipliedBy(item.getQuantity()));
        }
        return subTotal;
    }

    static class ExpandedCartItem {

        private final Product product;
        private final int quantity;

        ExpandedCartItem(Product product, int quantity) {

            this.product = product;
            this.quantity = quantity;
        }

        Product getProduct() {
            return product;
        }

        int getQuantity() {
            return quantity;
        }
    }
}
