package com.ab.cart.domain;

public class ExpandedCartItem extends CartItem {

    private final Product product;

    public ExpandedCartItem(Product product, int quantity) {
        super(product.getProductId(), quantity);
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
