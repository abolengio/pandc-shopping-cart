package com.ab.cart.rest.controller;

import com.ab.cart.config.spring.TestApplicationConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.Product;
import com.ab.cart.domain.ProductNotInShoppingCartException;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.domain.productcatalogue.ProductCatalogue;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static com.ab.cart.domain.builders.EffectivePriceProductBuilder.productWithId;
import static com.ab.cart.domain.builders.ExpandedCartItemBuilder.cartItem;
import static com.ab.cart.domain.builders.ReadableShoppingCartBuilder.shoppingCart;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.apache.commons.lang.StringUtils.replace;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class,TestApplicationConfig.class})
@WebAppConfiguration
public class ShoppingCartControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ReadableShoppingCartProvider mockReadableShoppingCartProvider;

    @Autowired
    private ProductCatalogue productCatalogue;

    @Autowired
    private WritableShoppingCart writableShoppingCart;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        reset(mockReadableShoppingCartProvider);
        reset(productCatalogue);
        reset(writableShoppingCart);
    }

    @Test
    public void shouldReturnNoItemsWhenCartIsEmpty() throws Exception{

        when(mockReadableShoppingCartProvider.getReadableShoppingCart())
                .thenReturn(shoppingCart().empty().build());

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.subTotal.amount", is(0.0)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")))
        ;
    }

    @Test
    public void shouldReturnCartWhenItIsNotEmpty() throws Exception{

     //   Product
        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).effectivePrice(3.90).build();
        EffectivePriceProduct product2 = productWithId("product2-id").name("product 2 name").price(8.50).effectivePrice(8.19).build();
        when(mockReadableShoppingCartProvider
                    .getReadableShoppingCart()).thenReturn(
                                                    shoppingCart().withItems(
                                                            cartItem().with(product1).quantity(1).build()
                                                            ,cartItem().with(product2).quantity(2).build()
                                                        )
                                                        .withSubTotal(23.89).build());

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].productId", is("product1-id")))
                .andExpect(jsonPath("$.items[0].quantity", is(1)))
                .andExpect(jsonPath("$.items[0].subTotal.amount", is(3.90)))
                .andExpect(jsonPath("$.items[0].subTotal.currency", is("EUR")))
                .andExpect(jsonPath("$.items[0].product.name", is("product 1")))
                .andExpect(jsonPath("$.items[0].product.price.amount", is(12.38)))
                .andExpect(jsonPath("$.items[0].product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.items[0].product.effectivePrice.amount", is(3.90)))
                .andExpect(jsonPath("$.items[0].product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.items[1].productId", is("product2-id")))
                .andExpect(jsonPath("$.items[1].quantity", is(2)))
                .andExpect(jsonPath("$.items[1].subTotal.amount", is(16.38)))
                .andExpect(jsonPath("$.items[1].subTotal.currency", is("EUR")))
                .andExpect(jsonPath("$.items[1].product.name", is("product 2 name")))
                .andExpect(jsonPath("$.items[1].product.price.amount", is(8.5)))
                .andExpect(jsonPath("$.items[1].product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.items[1].product.effectivePrice.amount", is(8.19)))
                .andExpect(jsonPath("$.items[1].product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.subTotal.amount", is(23.89)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR"))
                );
    }

    @Test
    public void shouldIncludeLinksWhenReturningCart() throws Exception{

     //   Product
        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).build();
        when(mockReadableShoppingCartProvider
                    .getReadableShoppingCart()).thenReturn(
                                                    shoppingCart().withItems(
                                                            cartItem().with(product1).quantity(1).build()
                                                        )
                                                        .withSubTotal(23.89).build());

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.links", hasSize(2)))
                .andExpect(jsonPath("$.links[0].href", is(UriFor.cart)))
                .andExpect(jsonPath("$.links[0].rel", is("self")))
                .andExpect(jsonPath("$.links[0].method", is("GET")))
                .andExpect(jsonPath("$.links[1].href", is(UriFor.cartItems)))
                .andExpect(jsonPath("$.links[1].rel", is("add-item")))
                .andExpect(jsonPath("$.links[1].method", is("POST")))
                .andExpect(jsonPath("$.items[0].links", hasSize(4)))
                .andExpect(jsonPath("$.items[0].links[0].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.items[0].links[0].rel", is("remove")))
                .andExpect(jsonPath("$.items[0].links[0].method", is("DELETE")))
                .andExpect(jsonPath("$.items[0].links[1].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.items[0].links[1].rel", is("update")))
                .andExpect(jsonPath("$.items[0].links[1].method", is("PUT")))
                .andExpect(jsonPath("$.items[0].links[2].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.items[0].links[2].rel", is("self")))
                .andExpect(jsonPath("$.items[0].links[2].method", is("GET")))
                .andExpect(jsonPath("$.items[0].links[3].href", is(UriFor.cart)))
                .andExpect(jsonPath("$.items[0].links[3].rel", is("container")))
                .andExpect(jsonPath("$.items[0].links[3].method", is("GET")))
                ;
    }

    @Test
    public void shouldIncludeLinksWhenReturningCartItem() throws Exception{

     //   Product
        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).build();
        when(mockReadableShoppingCartProvider
                    .getShoppingCartItem("product1-id")).thenReturn(
                                                            cartItem().with(product1).quantity(1).build()
                                );

        mockMvc.perform(get(uriForCartItemWithProductId("product1-id")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.links", hasSize(4)))
                .andExpect(jsonPath("$.links[0].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.links[0].rel", is("remove")))
                .andExpect(jsonPath("$.links[0].method", is("DELETE")))
                .andExpect(jsonPath("$.links[1].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.links[1].rel", is("update")))
                .andExpect(jsonPath("$.links[1].method", is("PUT")))
                .andExpect(jsonPath("$.links[2].href", is(uriForCartItemWithProductId("product1-id"))))
                .andExpect(jsonPath("$.links[2].rel", is("self")))
                .andExpect(jsonPath("$.links[2].method", is("GET")))
                .andExpect(jsonPath("$.links[3].href", is(UriFor.cart)))
                .andExpect(jsonPath("$.links[3].rel", is("container")))
                .andExpect(jsonPath("$.links[3].method", is("GET")))
                ;
    }

    @Test
    public void shouldReturnCartWhenSomeItemsHaveRebateDiscount() throws Exception{

        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).effectivePrice(3.20).build();
        EffectivePriceProduct product2 = productWithId("product2-id").name("product 2 name").price(8.50).effectivePrice(3.70)
                                    .rebateTimeframe().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        when(mockReadableShoppingCartProvider
                    .getReadableShoppingCart()).thenReturn(
                                                    shoppingCart().withItems(
                                                            cartItem().with(product1).quantity(1).build()
                                                            ,cartItem().with(product2).quantity(2).build()
                                                        )
                                                        .withSubTotal(23.89).build());

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].productId", is("product1-id")))
                .andExpect(jsonPath("$.items[0].quantity", is(1)))
                .andExpect(jsonPath("$.items[0].product.name", is("product 1")))
                .andExpect(jsonPath("$.items[0].product.price.amount", is(12.38)))
                .andExpect(jsonPath("$.items[0].product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.items[0].product.effectivePrice.amount", is(3.20)))
                .andExpect(jsonPath("$.items[0].product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.items[0].product.rebateTimeFrame").doesNotExist())
                .andExpect(jsonPath("$.items[1].productId", is("product2-id")))
                .andExpect(jsonPath("$.items[1].quantity", is(2)))
                .andExpect(jsonPath("$.items[1].product.name", is("product 2 name")))
                .andExpect(jsonPath("$.items[1].product.price.amount", is(8.5)))
                .andExpect(jsonPath("$.items[1].product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.items[1].product.effectivePrice.amount", is(3.70)))
                .andExpect(jsonPath("$.items[1].product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.items[1].product.rebateTimeFrame.start", is("2014-04-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.items[1].product.rebateTimeFrame.end", is("2014-05-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.subTotal.amount", is(23.89)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")))
        ;
    }

    @Test
    public void shouldAddItem() throws Exception{

        EffectivePriceProduct product = productWithId("product1-id").name("product name").price(8.90).effectivePrice(3.80)
                .rebateTimeframe().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        when(mockReadableShoppingCartProvider.getShoppingCartItem("product1-id")).thenReturn(
                cartItem().with(product).quantity(2).build()
        );
        when(productCatalogue.getProduct("product1-id")).thenReturn(new Product("product1-id", "name", somePrice()));

        mockMvc.perform(post(UriFor.cartItems)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":2}"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productId", is("product1-id")))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.subTotal.amount", is(7.60)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")))
                .andExpect(jsonPath("$.product.name", is("product name")))
                .andExpect(jsonPath("$.product.effectivePrice.amount", is(3.80)))
                .andExpect(jsonPath("$.product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.product.price.amount", is(8.90)))
                .andExpect(jsonPath("$.product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.start", is("2014-04-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.end", is("2014-05-01T12:37:00.000+01:00")))
                ;

        verify(writableShoppingCart).add("product1-id", 2);
    }

    @Test
    public void shouldReturnErrorIfContentTypeIsNotSetOnAddItemRequest() throws Exception{

        mockMvc.perform(post(UriFor.cartItems)
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":2}"))
                .andExpect(status().is(415))
                ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldReturnErrorIfContentTypeIsNotSetOnUpdateQuantityRequest() throws Exception{

        mockMvc.perform(put(uriForCartItemWithProductId("product1-id"))
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":2}"))
                .andExpect(status().is(415))
                ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldReturnValidationErrorWhenTryingToAddItemWithNegativeQuantity() throws Exception{

        when(productCatalogue.getProduct("product1-id")).thenReturn(new Product("product1-id", "name", somePrice()));

        mockMvc.perform(post(UriFor.cartItems)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":-1}"))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("quantity")))
                .andExpect(jsonPath("$.errors[0].message", is("Quantity should not be negative")))
                ;

        verifyZeroInteractions(writableShoppingCart);
    }

    private Money somePrice() {
        return Money.of(CurrencyUnit.EUR, 37.28);
    }

    @Test
    public void shouldReturnValidationErrorWhenTryingToAddNotExistingProduct() throws Exception{

        mockMvc.perform(post(UriFor.cartItems)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":1}"))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("productId")))
                .andExpect(jsonPath("$.errors[0].message", is("Product with id 'product1-id' does not exist in the product catalogue")))
                ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldReturnValidationErrorWhenQuantityIsNotANumber() throws Exception{

        mockMvc.perform(post(UriFor.cartItems)
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"productId\":\"2\"," +
                        "\"quantity\":\"blah\"}"))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldReturnSingleItem() throws Exception{

        EffectivePriceProduct product = productWithId("product1-id").name("product name").price(8.90).effectivePrice(3.80)
                .rebateTimeframe().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        when(mockReadableShoppingCartProvider.getShoppingCartItem("product1-id")).thenReturn(
                        cartItem().with(product).quantity(2).build()
                );

        mockMvc.perform(get(uriForCartItemWithProductId("product1-id")))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productId", is("product1-id")))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.subTotal.amount", is(7.60)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")))
                .andExpect(jsonPath("$.product.name", is("product name")))
                .andExpect(jsonPath("$.product.effectivePrice.amount", is(3.80)))
                .andExpect(jsonPath("$.product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.product.price.amount", is(8.90)))
                .andExpect(jsonPath("$.product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.start", is("2014-04-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.end", is("2014-05-01T12:37:00.000+01:00")))
                ;

    }

    @Test
    public void shouldReturnNotFoundResponseIfProductIsNotInTheCart() throws Exception{

        when(mockReadableShoppingCartProvider.getShoppingCartItem("product1-id")).thenThrow(new ProductNotInShoppingCartException("product1-id"));

        mockMvc.perform(get(uriForCartItemWithProductId("product1-id")))
                .andExpect(status().is(404))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Product with id 'product1-id' is not in the shopping cart")))
                ;

    }

    @Test
    public void shouldUpdateQuantity() throws Exception{
        EffectivePriceProduct product = productWithId("product1-id").name("single product name").price(8.90).effectivePrice(3.80)
                .rebateTimeframe().start("2014-04-01T12:37:00").end("2014-05-01T12:37:00").build();
        when(mockReadableShoppingCartProvider.getShoppingCartItem("product1-id")).thenReturn(
                cartItem().with(product).quantity(2).build()
        );
        when(productCatalogue.getProduct("product1-id")).thenReturn(new Product("product1-id", "name", somePrice()));
        mockMvc.perform(put(uriForCartItemWithProductId("product1-id"))
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"productId\":\"product1-id\"," +
                        "\"quantity\":7}"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productId", is("product1-id")))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.subTotal.amount", is(7.60)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")))
                .andExpect(jsonPath("$.product.effectivePrice.amount", is(3.80)))
                .andExpect(jsonPath("$.product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.product.price.amount", is(8.90)))
                .andExpect(jsonPath("$.product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.start", is("2014-04-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.end", is("2014-05-01T12:37:00.000+01:00")))
        ;

        verify(writableShoppingCart).updateQuantity("product1-id", 7);
    }

    @Test
    public void shouldReturnValidationErrorWhenTryingToUpdateItemForNotExistingProductAndQuantityIsNegative() throws Exception{

        mockMvc.perform(put(uriForCartItemWithProductId("product1-id"))
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"productId\":\"product1-id\"," +
                        "\"quantity\":-7}"))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].field", is("quantity")))
                .andExpect(jsonPath("$.errors[0].message", is("Quantity should not be negative")))
                .andExpect(jsonPath("$.errors[1].field", is("productId")))
                .andExpect(jsonPath("$.errors[1].message", is("Product with id 'product1-id' does not exist in the product catalogue")))
        ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldReturnValidationErrorWhenTryingToUpdateItemAndPathParameterDoesNotMatchProductIdInTheRequestBody() throws Exception{

        when(productCatalogue.getProduct("product1-id")).thenReturn(new Product("product1-id", "name", somePrice()));
        when(productCatalogue.getProduct("product2-id")).thenReturn(new Product("product2-id", "name", somePrice()));

        mockMvc.perform(put(uriForCartItemWithProductId("product1-id"))
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"productId\":\"product2-id\"," +
                        "\"quantity\":7}"))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("productId")))
                .andExpect(jsonPath("$.errors[0].message", is("ProductId specified in the url (product1-id) does not " +
                                                                "match with productId in the body (product2-id). They must be the same.")))
        ;

        verifyZeroInteractions(writableShoppingCart);
    }

    @Test
    public void shouldRemoveItem() throws Exception{

        when(productCatalogue.getProduct("product1-id")).thenReturn(new Product("product1-id", "name", somePrice()));

        mockMvc.perform(delete(uriForCartItemWithProductId("product1-id")))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].href", is(UriFor.cart)))
                .andExpect(jsonPath("$.links[0].rel", is("container")))
                .andExpect(jsonPath("$.links[0].method", is("GET")))
        ;

        verify(writableShoppingCart).remove("product1-id");
    }

    @Test
    public void shouldReturnValidationErrorWhenTryingToRemoveItemForProductWhichDoesNotExist() throws Exception{

        mockMvc.perform(delete(uriForCartItemWithProductId("product1-id")))
                .andExpect(status().is(400))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field", is("productId")))
                .andExpect(jsonPath("$.errors[0].message", is("Product with id 'product1-id' does not exist in the product catalogue")))
        ;

        verifyZeroInteractions(writableShoppingCart);
    }

    private String uriForCartItemWithProductId(String productId) {
        return replace(UriFor.cartItem, "{productId}" , productId);
    }
}
