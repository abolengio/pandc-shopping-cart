package com.ab.cart.config.spring;

import com.ab.cart.domain.Clock;
import com.ab.cart.domain.EffectivePriceProductProvider;
import com.ab.cart.domain.PricingStrategy;
import com.ab.cart.domain.Product;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.converters.CartItemToExpandedCartItemTransformer;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.repository.ShoppingCartItemsRepository;
import com.ab.cart.repository.impl.ProductCsvEntryParser;
import com.ab.cart.repository.impl.ProductCsvFileReader;
import com.ab.cart.repository.impl.ProductRebateTimeFrameParser;
import com.ab.cart.repository.impl.StaticProductRepository;
import com.ab.cart.repository.impl.eventsourced.AggregatingShoppingCartFactory;
import com.ab.cart.repository.impl.eventsourced.EventSourcingFileShoppingCartReaderWriter;
import com.ab.cart.repository.impl.eventsourced.EventSourcingShoppingCartItemsRepository;
import com.ab.cart.repository.impl.eventsourced.ShoppingCartCommandSerializerDeserializer;
import com.ab.cart.utils.FileLineWriter;
import com.ab.cart.utils.FileReaderFactory;
import com.ab.cart.utils.FileWriterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Autowired
    Environment environment;

    @Bean
    @Autowired
    public ReadableShoppingCartProvider readableShoppingCartProvider(ShoppingCartItemsRepository shoppingCartItemsRepository,
                                                                     CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer) {
        return new ReadableShoppingCartProvider(shoppingCartItemsRepository, cartItemToExpandedCartItemTransformer);
    }

    @Bean
    @Autowired
    public ShoppingCartItemsRepository shoppingCartItemsRepository() {
        return new EventSourcingShoppingCartItemsRepository(new AggregatingShoppingCartFactory()
                , eventSourcingFileShoppingCartReaderWriter());
    }

    @Bean
    @Autowired
    public EventSourcingFileShoppingCartReaderWriter eventSourcingFileShoppingCartReaderWriter() {
        return new EventSourcingFileShoppingCartReaderWriter(environment,
                                                        fileReaderProvider(),
                                                        new FileLineWriter(fileWriterProvider(), environment.getProperty(EventSourcingFileShoppingCartReaderWriter.SHOPPING_CART_FILE_PATH_PROPERTY)),
                                                        new ShoppingCartCommandSerializerDeserializer());
    }

    @Bean
    @Autowired
    public CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer(EffectivePriceProductProvider effectivePriceProductProvider) {
        return new CartItemToExpandedCartItemTransformer(effectivePriceProductProvider);
    }

    @Bean
    public PricingStrategy pricingStrategy() {
        return new PricingStrategy(new Clock());
    }

    @Bean
    @Autowired
    public EffectivePriceProductProvider effectivePriceProductProvider(ProductCatalogue productCatalogue) {
        return new EffectivePriceProductProvider(productCatalogue, pricingStrategy());
    }

    @Bean
    public FileWriterFactory fileWriterProvider() {
        return new FileWriterFactory();
    }

    @Bean
    public ProductRebateTimeFrameParser rebateTimeFrameParser() {
        return new ProductRebateTimeFrameParser();
    }

    @Bean
    public ProductCsvEntryParser productCsvEntryParser() {
        return new ProductCsvEntryParser(rebateTimeFrameParser());
    }

    @Bean
    public FileReaderFactory fileReaderProvider() {
        return new FileReaderFactory();
    }

    @Bean
    @Autowired
    public ProductCsvFileReader csvFileProductProvider(Environment environment) {
        return new ProductCsvFileReader(environment, fileReaderProvider(), productCsvEntryParser());
    }

    @Bean
    @Autowired
    public ProductCatalogue productCatalogue(Environment environment) throws IOException {
        List<Product> products = csvFileProductProvider(environment).read();
        return new StaticProductRepository(products);
    }

}
