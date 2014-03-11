package com.ab.cart.repository;

import com.ab.cart.domain.CartItem;

import java.util.List;

public interface ShoppingCartItemsRepository {
    List<CartItem> getShoppingCartItems();
}
