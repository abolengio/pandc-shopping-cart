package com.ab.cart.config.spring;

import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import static org.mockito.Mockito.mock;

@Configuration
@PropertySource("test.properties")
@Import(ApplicationConfig.class)
public class TestApplicationConfig {

    @Bean
    ReadableShoppingCartProvider readableShoppingCartProvider() {
        return mock(ReadableShoppingCartProvider.class);
    }

    @Bean
    ProductCatalogue productCatalogue() {
        return mock(ProductCatalogue.class);
    }

    @Bean
    WritableShoppingCart writableShoppingCart() {
        return mock(WritableShoppingCart.class);
    }

}
