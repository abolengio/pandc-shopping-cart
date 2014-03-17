package com.ab.cart.domain;

import com.ab.cart.domain.builders.ReadableShoppingCartBuilder;
import com.ab.cart.domain.converters.CartItemToExpandedCartItemTransformer;
import com.ab.cart.repository.ShoppingCartItemsRepository;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
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

    public ReadableShoppingCart getReadableShoppingCart() {
        List<CartItem> shoppingCartItems = shoppingCartItemsRepository.getShoppingCartItems();
        Iterable<ExpandedCartItem> expandedCartItems = enhanceCartItemsWithProductAndPriceInfo(shoppingCartItems);

        return new ReadableShoppingCartBuilder().withItems(expandedCartItems).build();
    }

    public ExpandedCartItem getShoppingCartItem(final String productId) {
        List<CartItem> allShoppingCartItems = shoppingCartItemsRepository.getShoppingCartItems();
        Iterable<CartItem> cartItemForSpecifiedProduct = filter(allShoppingCartItems, new Predicate<CartItem>() {
            @Override
            public boolean apply(CartItem cartItem) {
                return Objects.equal(cartItem.getProductId(), productId);
            }
        });
        Iterator<ExpandedCartItem> resultIterator = enhanceCartItemsWithProductAndPriceInfo(cartItemForSpecifiedProduct).iterator();
        if (resultIterator.hasNext()) {
            return resultIterator.next();
        } else {
            throw new ProductNotInShoppingCartException(productId);
        }
    }

    private Iterable<ExpandedCartItem> enhanceCartItemsWithProductAndPriceInfo(Iterable<CartItem> shoppingCartItems) {
        return transform(shoppingCartItems, cartItemToExpandedCartItemTransformer);
    }
}
