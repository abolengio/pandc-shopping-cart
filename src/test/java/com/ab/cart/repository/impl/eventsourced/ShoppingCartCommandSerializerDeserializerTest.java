package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartCommandSerializerDeserializerTest {

    @Mock
    WritableShoppingCart writableShoppingCart;

    ShoppingCartCommandSerializerDeserializer commandEncoderDecoder = new ShoppingCartCommandSerializerDeserializer();

    @Test
    public void shouldReadAddCommand() {
        commandEncoderDecoder.read("ADD,product-id-1,56", writableShoppingCart);
        verify(writableShoppingCart).add("product-id-1", 56);
    }

    @Test
    public void shouldReadUpdateQuantityCommand() {
        commandEncoderDecoder.read("UPDATE_QUANTITY,product-id-2,8", writableShoppingCart);
        verify(writableShoppingCart).updateQuantity("product-id-2", 8);
    }

    @Test
    public void shouldReadRemoveCommand() {
        commandEncoderDecoder.read("REMOVE,product-id-3", writableShoppingCart);
        verify(writableShoppingCart).remove("product-id-3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfUnrecognisedCommandIsEncountered() {
        commandEncoderDecoder.read("SOMETHING,product-id-3,8", writableShoppingCart);
    }

    @Test
    public void shouldWriteRemoveCommand() {
        assertThat(commandEncoderDecoder.removeCommandFor("blah-id"), is("REMOVE,blah-id"));
    }

    @Test
    public void shouldWriteAddCommand() {
        assertThat(commandEncoderDecoder.addCommandFor("blah-id", 89), is("ADD,blah-id,89"));
    }

    @Test
    public void shouldWriteUpdateCommand() {
        assertThat(commandEncoderDecoder.updateQuantityCommandFor("blah-id", 89), is("UPDATE_QUANTITY,blah-id,89"));
    }

}
