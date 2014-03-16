package com.ab.cart.repository;

import com.ab.cart.domain.Product;

import java.util.List;

public interface ProductListReader {
    List<Product> read();
}
