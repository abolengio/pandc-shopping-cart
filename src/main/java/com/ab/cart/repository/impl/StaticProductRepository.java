package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticProductRepository implements ProductCatalogue {

    private Map<String, Product> productMap;

    public StaticProductRepository(List<Product> products) {
        productMap = new HashMap<>();
        for(Product product : products) {
            productMap.put(product.getProductId(), product);
        }
    }

    @Override
    public Product getProduct(String productId) {
        return productMap.get(productId);
    }
}
