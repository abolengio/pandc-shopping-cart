package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@RunWith(MockitoJUnitRunner.class)
public class AggregatingShoppingCartTest {

    @Test
    public void shouldAddFirstItem() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Some-id-123", 1);
        assertThat(cart.getItems(), hasSize(1));
        assertThat(cart.getItems().get(0).getQuantity(), is(1));
    }

    @Test
    public void shouldAddAdditionalItem() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Some-id-123", 1);
        cart.add("Another-id-566", 1);
        assertThat(cart.getItems(), hasSize(2));

        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Some-id-123"));
        assertThat(someCartItem.getQuantity(), is(1));
        CartItem anotherCartItem = cart.getItems().get(1);
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldAggregateItemsForTheSameProduct() {
        //given
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Id-of-product-to-be-added-twice-123", 1);
        cart.add("Another-id-566", 1);
        //when
        cart.add("Id-of-product-to-be-added-twice-123", 1);
        //then
        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Id-of-product-to-be-added-twice-123"));
        assertThat(someCartItem.getQuantity(), is(2));
        CartItem anotherCartItem = cart.getItems().get(1);
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldDeleteItem() {
        //given
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Id-of-product-to-be-removed", 2);
        cart.add("Another-id-566", 82);
        //when
        cart.remove("Id-of-product-to-be-removed");
        //then
        assertThat(cart.getItems(), hasSize(1));
        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Another-id-566"));
        assertThat(someCartItem.getQuantity(), is(82));
    }

    @Test
    public void shouldUpdateQuantity() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Id-of-product-1", 2);
        cart.add("Another-id-566", 82);
        //when
        cart.updateQuantity("Another-id-566", 1);
        //then
        assertThat(cart.getItems(), hasSize(2));
        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Id-of-product-1"));
        assertThat(someCartItem.getQuantity(), is(2));
        CartItem anotherCartItem = cart.getItems().get(1);
        assertThat(anotherCartItem.getProductId(), is("Another-id-566"));
        assertThat(anotherCartItem.getQuantity(), is(1));
    }

    @Test
    public void shouldAddWhenProductIsNotInCartAndUpdatingQuantity() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        //when
        cart.updateQuantity("id-566", 7);
        //then
        CartItem anotherCartItem = cart.getItems().get(0);
        assertThat(anotherCartItem.getProductId(), is("id-566"));
        assertThat(anotherCartItem.getQuantity(), is(7));
    }

    @Test
    public void shouldRemoveItemWhenQuantityUpdatedToZero() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Id-of-product-1", 2);
        cart.add("Another-id-566", 82);
        //when
        cart.updateQuantity("Another-id-566", 0);
        //then
        assertThat(cart.getItems(), hasSize(1));
        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Id-of-product-1"));
    }

    @Test
    public void shouldRemoveItemWhenQuantityUpdatedToNegativeNumber() {
        AggregatingShoppingCart cart = new AggregatingShoppingCart();
        cart.add("Id-of-product-1", 2);
        cart.add("Another-id-566", 82);
        //when
        cart.updateQuantity("Another-id-566", -1);
        //then
        assertThat(cart.getItems(), hasSize(1));
        CartItem someCartItem = cart.getItems().get(0);
        assertThat(someCartItem.getProductId(), is("Id-of-product-1"));
    }

}
