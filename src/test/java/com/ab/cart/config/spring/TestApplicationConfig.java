package com.ab.cart.config.spring;

import com.ab.cart.repository.ShoppingCartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestApplicationConfig {

    @Bean
    ShoppingCartRepository shoppingCartRepository() {
        return mock(ShoppingCartRepository.class);
    }

}
