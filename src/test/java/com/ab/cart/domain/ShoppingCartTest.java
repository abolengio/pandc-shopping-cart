package com.ab.cart.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.fail;

public class ShoppingCartTest {

    @Test
    public void shouldAddFirstItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Some-id-123", 1);
        assertThat(cart.getItems(), hasSize(1));
        assertThat(cart.getItem("Some-id-123").getQuantity(), is(1));
    }

    @Test
    public void shouldAddAdditionalItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Some-id-123", 1);
        cart.addItem("Another-id-566", 1);
        assertThat(cart.getItems(), hasSize(2));

        CartItem someCartItem = cart.getItem("Some-id-123");
        assertThat(someCartItem.getProductId(), is("Some-id-123"));
        assertThat(someCartItem.getQuantity(), is(1));
        CartItem anotherCartItem = cart.getItem("Another-id-566");
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldAggregateItemsForTheSameProduct() {
        //given
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("Id-of-product-to-be-added-twice-123", 1);
        cart.addItem("Another-id-566", 1);
        //when
        cart.addItem("Id-of-product-to-be-added-twice-123", 1);
        //then
        CartItem someCartItem = cart.getItem("Id-of-product-to-be-added-twice-123");
        assertThat(someCartItem.getProductId(), is("Id-of-product-to-be-added-twice-123"));
        assertThat(someCartItem.getQuantity(), is(2));
        CartItem anotherCartItem = cart.getItem("Another-id-566");
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldReturnSubTotal() {
        fail();
    }

}
