package com.ab.cart.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("integration.test.properties")
@Import(ApplicationConfig.class)
public class IntegrationTestConfig {

}
