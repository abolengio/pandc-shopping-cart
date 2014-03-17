package com.ab.cart.repository.impl;

import com.ab.cart.config.spring.ApplicationConfig;
import com.ab.cart.config.spring.IntegrationTestPropertiesConfig;
import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.apache.commons.io.FileUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, IntegrationTestPropertiesConfig.class})
public class CsvFileProductRepositoryIntegrationTest {

    @Autowired
    ProductCatalogue productCatalogue;

    @Autowired
    private Environment environment;

    private void givenProductFileWithContent(String content) throws IOException {
        String productFilePath = environment.getProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY);
        FileUtils.writeStringToFile(new File(productFilePath), content, Charset.forName("UTF-8"));
    }

    @Test
    public void shouldReadDataFromFile() throws IOException {
        givenProductFileWithContent("1001,test dress,28.9,\n" +
                "test-id,Test Green Shirt,9.90,\n");
        Product product = productCatalogue.getProduct("test-id");
        assertThat(product.getName(), is("Test Green Shirt"));
        assertThat(product.getPrice(), is(Money.of(CurrencyUnit.EUR, 9.9)));
    }

}
