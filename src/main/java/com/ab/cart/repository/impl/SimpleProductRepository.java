package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.repository.ProductListReader;

import java.util.HashMap;
import java.util.Map;

public class SimpleProductRepository implements ProductCatalogue {

    private ProductListReader productListReader;

    public SimpleProductRepository(ProductListReader productListReader) {
        this.productListReader = productListReader;
    }

    @Override
    public Product getProduct(String productId) {
        return getProductMap().get(productId);
    }

    private Map<String, Product> getProductMap() {
        Map<String, Product> productMap = new HashMap<>();
        for(Product product : productListReader.read()) {
            productMap.put(product.getProductId(), product);
        }
        return productMap;
    }
}
