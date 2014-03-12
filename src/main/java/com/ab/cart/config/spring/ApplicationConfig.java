package com.ab.cart.config.spring;

import com.ab.cart.domain.EffectivePriceProductProvider;
import com.ab.cart.domain.Product;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.converters.CartItemToExpandedCartItemTransformer;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import com.ab.cart.repository.ShoppingCartItemsRepository;
import com.ab.cart.repository.impl.CsvFileProductProvider;
import com.ab.cart.repository.impl.ProductCsvEntryParser;
import com.ab.cart.repository.impl.ProductRebateTimeFrameParser;
import com.ab.cart.repository.impl.StaticProductRepository;
import com.ab.cart.repository.impl.eventsourced.AggregatingShoppingCartFactory;
import com.ab.cart.repository.impl.eventsourced.EventSourcingFileShoppingCartReaderWriter;
import com.ab.cart.repository.impl.eventsourced.EventSourcingShoppingCartItemsRepository;
import com.ab.cart.repository.impl.eventsourced.ShoppingCartCommandSerializerDeserializer;
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
    @Autowired
    public ReadableShoppingCartProvider readableShoppingCartProvider(ShoppingCartItemsRepository shoppingCartItemsRepository,
                                                                     CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer) {
        return new ReadableShoppingCartProvider(shoppingCartItemsRepository, cartItemToExpandedCartItemTransformer);
    }

    @Bean
    @Autowired
    public ShoppingCartItemsRepository shoppingCartItemsRepository(Environment environment) {
        return new EventSourcingShoppingCartItemsRepository(new AggregatingShoppingCartFactory()
                , new EventSourcingFileShoppingCartReaderWriter(environment,
                                                                fileReaderProvider(),
                                                                new ShoppingCartCommandSerializerDeserializer()));
    }

    @Bean
    @Autowired
    public CartItemToExpandedCartItemTransformer cartItemToExpandedCartItemTransformer(EffectivePriceProductProvider effectivePriceProductProvider) {
        return new CartItemToExpandedCartItemTransformer(effectivePriceProductProvider);
    }

    @Bean
    @Autowired
    public EffectivePriceProductProvider effectivePriceProductProvider(ProductCatalogue productCatalogue) {
        return new EffectivePriceProductProvider(productCatalogue);
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
    public FileReaderProvider fileReaderProvider() {
        return new FileReaderProvider();
    }

    @Bean
    @Autowired
    public CsvFileProductProvider csvFileProductProvider(Environment environment) {
        return new CsvFileProductProvider(environment, fileReaderProvider(), productCsvEntryParser());
    }

    @Bean
    @Autowired
    public ProductCatalogue productCatalogue(Environment environment) throws IOException {
        List<Product> products = csvFileProductProvider(environment).parse();
        return new StaticProductRepository(products);
    }

}
