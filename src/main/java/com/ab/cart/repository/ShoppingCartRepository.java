package com.ab.cart.repository;

import com.ab.cart.domain.CartItem;

import java.util.List;

public interface ShoppingCartRepository {
    List<CartItem> listItems();
}
