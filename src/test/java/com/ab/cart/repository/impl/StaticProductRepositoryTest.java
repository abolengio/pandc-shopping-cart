package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import org.joda.money.Money;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.joda.money.CurrencyUnit.EUR;

public class StaticProductRepositoryTest {

    @Test
    public void shouldReturnProductsPassedAtCreation() {
        //given
        List<Product> products = new ArrayList<>();
        Product product1 = new Product("product-id-1", "some product name", Money.of(EUR, 6.56));
        Product product2 = new Product("product-id-2", "some product name", Money.of(EUR, 2726.1));
        products.add(product1);
        products.add(product2);
        //when
        StaticProductRepository repository = new StaticProductRepository(products);
        //then
        assertThat(repository.getProduct("product-id-1"), is(product1));
        assertThat(repository.getProduct("product-id-2"), is(product2));

    }
}
