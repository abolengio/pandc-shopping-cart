package com.ab.cart.domain;

import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

public class EffectivePriceProductProvider {

    private ProductCatalogue productCatalogue;
    private PricingStrategy pricingStrategy;

    @Autowired
    public EffectivePriceProductProvider(ProductCatalogue productCatalogue, PricingStrategy pricingStrategy) {
        this.productCatalogue = productCatalogue;
        this.pricingStrategy = pricingStrategy;
    }

    public EffectivePriceProduct getProduct(String productId) {
        Product product = productCatalogue.getProduct(productId);
        if(product == null) throw new ProductDoesNotExistException(productId);
        Money effectivePrice = pricingStrategy.getEffectivePrice(product);

        return new EffectivePriceProduct(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        product.getRebateTimeFrame(),
                        effectivePrice
                );
    }
}
