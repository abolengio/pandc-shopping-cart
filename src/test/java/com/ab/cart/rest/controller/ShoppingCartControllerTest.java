package com.ab.cart.rest.controller;

import com.ab.cart.config.spring.TestApplicationConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.domain.EffectivePriceProduct;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.subTotal", is(0.0))); //todo change formatting
    }

    @Test
    public void shouldReturnCartWhenItIsNotEmpty() throws Exception{

     //   Product
        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).build();
        EffectivePriceProduct product2 = productWithId("product2-id").name("product 2 name").price(8.50)
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
                .andExpect(jsonPath("$.items[1].productId", is("product2-id")))
                .andExpect(jsonPath("$.items[1].quantity", is(2)))
                .andExpect(jsonPath("$.subTotal", is(23.89)));
    }

    @Test
    @Ignore
    public void shouldReturnCartWhenSomeItemsHaveRebateDiscount() throws Exception{

        EffectivePriceProduct product1 = productWithId("product1-id").name("product 1").price(12.38).build();
        EffectivePriceProduct product2 = productWithId("product2-id").name("product 2 name").price(8.50)
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
                .andExpect(jsonPath("$.subTotal", is(23.89)));
    }



}
