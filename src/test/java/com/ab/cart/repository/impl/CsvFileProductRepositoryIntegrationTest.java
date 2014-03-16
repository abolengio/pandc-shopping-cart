package com.ab.cart.repository.impl;

import com.ab.cart.config.spring.TestApplicationConfig;
import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplicationConfig.class})
public class CsvFileProductRepositoryIntegrationTest {

    @Autowired
    ProductCatalogue productCatalogue;

    @Test
    public void shouldReadDataFromFile() {
        Product product = productCatalogue.getProduct("1002-test");
        assertThat(product.getName(), is("Test Green Shirt"));
        assertThat(product.getPrice(), is(Money.of(CurrencyUnit.EUR, 9.9)));
    }

    //todo why so few test cases ?
    /*
    1001,Dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00
    1002,Green Shirt,9.90,
     */

}
