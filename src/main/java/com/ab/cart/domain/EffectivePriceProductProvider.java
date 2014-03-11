package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.springframework.beans.factory.annotation.Autowired;

public class EffectivePriceProductProvider {

    private ProductCatalogue productCatalogue;

    @Autowired
    public EffectivePriceProductProvider(ProductCatalogue productCatalogue) {
        this.productCatalogue = productCatalogue;
    }

    public EffectivePriceProduct getProduct(String productId) {
        Product product = productCatalogue.getProduct(productId);

        return new EffectivePriceProduct(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        product.getRebateTimeFrame()
                );
    }
}
