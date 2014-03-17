package com.ab.cart.rest.controller;

import com.ab.cart.config.spring.TestApplicationConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
    private WritableShoppingCart writableShoppingCart;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        reset(mockReadableShoppingCartProvider);
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

    //todo test case when Content Type is NOT supplied in PUT request

    @Test
    public void shouldAddItem() throws Exception{

        mockMvc.perform(post(UriFor.cartItems)
                                .contentType(APPLICATION_JSON_UTF8)
                                .content("{\"productId\":\"product1-id\"," +
                                        "\"quantity\":2}"))
                .andExpect(status().is(303))
                .andExpect(header().string("Location",UriFor.cart))
                ;

        verify(writableShoppingCart).add("product1-id", 2);
    }

    @Test
    public void shouldReturnSingleItem() throws Exception{

        EffectivePriceProduct product = productWithId("product1-id").name("single product name").price(8.90).effectivePrice(3.80)
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
                .andExpect(jsonPath("$.product.effectivePrice.amount", is(3.80)))
                .andExpect(jsonPath("$.product.effectivePrice.currency", is("EUR")))
                .andExpect(jsonPath("$.product.price.amount", is(8.90)))
                .andExpect(jsonPath("$.product.price.currency", is("EUR")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.start", is("2014-04-01T12:37:00.000+01:00")))
                .andExpect(jsonPath("$.product.rebateTimeFrame.end", is("2014-05-01T12:37:00.000+01:00")))
                ;

    }


    //todo test validation
    //todo test update and delete
    private String uriForCartItemWithProductId(String productId) {
        return replace(UriFor.cartItem, "{productId}" , productId);
    }
}
