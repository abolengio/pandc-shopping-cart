package com.ab.cart.config.spring;

import com.ab.cart.domain.ReadableShoppingCartProvider;
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

}
