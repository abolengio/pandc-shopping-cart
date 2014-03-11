package com.ab.cart.domain;

import com.ab.cart.domain.builders.ReadableShoppingCartBuilder;
import com.ab.cart.domain.converters.CartItemToExpandedCartItemTransformer;
import com.ab.cart.repository.ShoppingCartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Iterables.transform;

public class ReadableShoppingCartProvider {

    private final ShoppingCartItemsRepository shoppingCartItemsRepository;
    private final CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer;

    @Autowired
    public ReadableShoppingCartProvider(ShoppingCartItemsRepository shoppingCartItemsRepository,
                                        CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer) {

        this.shoppingCartItemsRepository = shoppingCartItemsRepository;
        this.cartItemToExpandedCartItemTransformer = cartItemToExpandedCartItemTransformer;
    }


    //todo handle non existing products
    public ReadableShoppingCart getReadableShoppingCart() {
        Iterable<ExpandedCartItem> expandedCartItems = transform(
                                                            shoppingCartItemsRepository.getShoppingCartItems(),
                                                            cartItemToExpandedCartItemTransformer);

        return new ReadableShoppingCartBuilder().withItems(expandedCartItems).build();
    }
}
