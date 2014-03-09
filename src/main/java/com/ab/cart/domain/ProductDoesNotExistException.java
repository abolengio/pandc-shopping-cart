package com.ab.cart.domain;

import static java.lang.String.format;

public class ProductDoesNotExistException extends RuntimeException {

    public ProductDoesNotExistException(String requestedProductId) {
        super(format("Product with id '%s' does not exist in the product catalogue", requestedProductId));
    }
}
