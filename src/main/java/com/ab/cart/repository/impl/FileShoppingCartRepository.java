package com.ab.cart.repository.impl;

import com.ab.cart.domain.CartItem;
import com.ab.cart.repository.ShoppingCartRepository;

import java.util.Collections;
import java.util.List;

public class FileShoppingCartRepository implements ShoppingCartRepository {

    @Override
    public List<CartItem> listItems() {
        return Collections.emptyList();
    }

}
