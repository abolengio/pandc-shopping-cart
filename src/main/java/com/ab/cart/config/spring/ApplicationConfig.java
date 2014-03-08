package com.ab.cart.config.spring;

import com.ab.cart.repository.ShoppingCartRepository;
import com.ab.cart.repository.impl.FileShoppingCartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    ShoppingCartRepository shoppingCartRepository() {
        return new FileShoppingCartRepository();
    }


}
