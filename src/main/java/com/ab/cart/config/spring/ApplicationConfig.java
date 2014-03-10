package com.ab.cart.config.spring;

import com.ab.cart.domain.Product;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.repository.ShoppingCartRepository;
import com.ab.cart.repository.impl.CsvFileProductProvider;
import com.ab.cart.repository.impl.FileShoppingCartRepository;
import com.ab.cart.repository.impl.ProductCsvEntryParser;
import com.ab.cart.repository.impl.ProductRebateTimeFrameParser;
import com.ab.cart.repository.impl.StaticProductRepository;
import com.ab.cart.utils.FileReaderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    ShoppingCartRepository shoppingCartRepository() {
        return new FileShoppingCartRepository();
    }

    @Bean
    ProductRebateTimeFrameParser rebateTimeFrameParser() {
        return new ProductRebateTimeFrameParser();
    }

    @Bean
    ProductCsvEntryParser productCsvEntryParser() {
        return new ProductCsvEntryParser(rebateTimeFrameParser());
    }

    @Bean
    public FileReaderProvider fileReaderProvider() {
        return new FileReaderProvider();
    }

    @Bean
    @Autowired
    CsvFileProductProvider csvFileProductProvider(Environment environment) {
        return new CsvFileProductProvider(environment, fileReaderProvider(), productCsvEntryParser());
    }

    @Bean
    @Autowired
    ProductCatalogue productCatalogue(Environment environment) throws IOException {
        List<Product> products = csvFileProductProvider(environment).parse();
        return new StaticProductRepository(products);
    }

}
