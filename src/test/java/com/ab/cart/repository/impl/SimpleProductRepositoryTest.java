package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.repository.ProductListReader;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.joda.money.CurrencyUnit.EUR;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleProductRepositoryTest {

    @Mock
    ProductListReader productListReader;

    @Test
    public void shouldReturnProductsPassedAtCreation() throws IOException {
        //given
        List<Product> products = new ArrayList<>();
        Product product1 = new Product("product-id-1", "some product name", Money.of(EUR, 6.56));
        Product product2 = new Product("product-id-2", "some product name", Money.of(EUR, 2726.1));
        products.add(product1);
        products.add(product2);
        when(productListReader.read()).thenReturn(products);
        //when
        SimpleProductRepository repository = new SimpleProductRepository(productListReader);
        //then
        assertThat(repository.getProduct("product-id-1"), is(product1));
        assertThat(repository.getProduct("product-id-2"), is(product2));

    }
}
