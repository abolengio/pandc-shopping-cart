package com.ab.cart.domain;

import com.ab.cart.domain.converters.CartItemToExpandedCartItemTransformer;
import com.ab.cart.repository.ShoppingCartItemsRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadableShoppingCartProviderTest {

    @Mock
    ShoppingCartItemsRepository shoppingCartItemsRepository;

    @Mock
    CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer;

    @Test
    public void shouldAddExpandedProductsToCartItems() {
        //given
        CartItem cartItem1 = mock(CartItem.class);
        CartItem cartItem2 = mock(CartItem.class);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem1);
        items.add(cartItem2);
        when(shoppingCartItemsRepository.getShoppingCartItems()).thenReturn(items);
        ExpandedCartItem expandedCartItem1 = mock(ExpandedCartItem.class);
        ExpandedCartItem expandedCartItem2 = mock(ExpandedCartItem.class);
        when(cartItemToExpandedCartItemTransformer.apply(cartItem1)).thenReturn(expandedCartItem1);
        when(cartItemToExpandedCartItemTransformer.apply(cartItem2)).thenReturn(expandedCartItem2);
        when(expandedCartItem1.getSubTotal()).thenReturn(Money.of(CurrencyUnit.EUR, 30));
        when(expandedCartItem2.getSubTotal()).thenReturn(Money.of(CurrencyUnit.EUR, 10));
        //when
        ReadableShoppingCartProvider cartProvider = new ReadableShoppingCartProvider(shoppingCartItemsRepository, cartItemToExpandedCartItemTransformer);
        ReadableShoppingCart readableShoppingCart = cartProvider.getReadableShoppingCart();
        List<ExpandedCartItem> expandedCartItems = readableShoppingCart.getItems();
        //then
        assertThat(expandedCartItems.get(0), is(expandedCartItem1));
        assertThat(expandedCartItems.get(1), is(expandedCartItem2));
    }

    @Test
    public void shouldReturnEmptyCartWhenThereAreNoItemsInRepository() {
        //given
        List<CartItem> items = new ArrayList<>();
        when(shoppingCartItemsRepository.getShoppingCartItems()).thenReturn(items);
        //when
        ReadableShoppingCartProvider cartProvider = new ReadableShoppingCartProvider(shoppingCartItemsRepository, cartItemToExpandedCartItemTransformer);
        ReadableShoppingCart readableShoppingCart = cartProvider.getReadableShoppingCart();
        List<ExpandedCartItem> expandedCartItems = readableShoppingCart.getItems();
        //then
        assertThat(expandedCartItems, hasSize(0));
        assertThat(readableShoppingCart.getSubTotal(), is(Money.of(CurrencyUnit.EUR, 0)));
    }

    @Test
    public void shouldCalculateSubTotalWhenThereAreMoreThanOneItem() {
        //given
        CartItem cartItem1 = mock(CartItem.class);
        CartItem cartItem2 = mock(CartItem.class);
        CartItem cartItem3 = mock(CartItem.class);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem1);
        items.add(cartItem2);
        items.add(cartItem3);
        when(shoppingCartItemsRepository.getShoppingCartItems()).thenReturn(items);
        ExpandedCartItem expandedCartItem1 = mock(ExpandedCartItem.class);
        ExpandedCartItem expandedCartItem2 = mock(ExpandedCartItem.class);
        ExpandedCartItem expandedCartItem3 = mock(ExpandedCartItem.class);
        when(expandedCartItem1.getSubTotal()).thenReturn(Money.of(CurrencyUnit.EUR, 30));
        when(expandedCartItem2.getSubTotal()).thenReturn(Money.of(CurrencyUnit.EUR, 10));
        when(expandedCartItem3.getSubTotal()).thenReturn(Money.of(CurrencyUnit.EUR, 5));
        when(cartItemToExpandedCartItemTransformer.apply(cartItem1)).thenReturn(expandedCartItem1);
        when(cartItemToExpandedCartItemTransformer.apply(cartItem2)).thenReturn(expandedCartItem2);
        when(cartItemToExpandedCartItemTransformer.apply(cartItem3)).thenReturn(expandedCartItem3);
        //when
        ReadableShoppingCartProvider cartProvider = new ReadableShoppingCartProvider(shoppingCartItemsRepository, cartItemToExpandedCartItemTransformer);
        ReadableShoppingCart readableShoppingCart = cartProvider.getReadableShoppingCart();
        //then
        assertThat(readableShoppingCart.getSubTotal(), is(Money.of(CurrencyUnit.EUR, 45)));
    }

}
