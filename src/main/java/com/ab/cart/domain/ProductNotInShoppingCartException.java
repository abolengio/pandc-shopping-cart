package com.ab.cart.domain;

import static java.lang.String.format;

public class ProductNotInShoppingCartException extends RuntimeException {

    public ProductNotInShoppingCartException(String requestedProductId) {
        super(format("Product with id '%s' is not in the shopping cart", requestedProductId));
    }
}
