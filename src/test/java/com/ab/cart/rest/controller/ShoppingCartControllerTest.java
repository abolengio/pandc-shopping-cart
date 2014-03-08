package com.ab.cart.rest.controller;

import com.ab.cart.config.spring.TestApplicationConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.domain.CartItem;
import com.ab.cart.repository.ShoppingCartRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
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

    private static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ShoppingCartRepository mockShoppingCartRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        reset(mockShoppingCartRepository);
    }

    @Test
    public void shouldReturnNoItemsWhenTheAreNoItemsInRepository() throws Exception{

        when(mockShoppingCartRepository.listItems()).thenReturn(Collections.<CartItem>emptyList());

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.total", is(0)));
    }

    @Test
    public void shouldAddItemToTheCart() throws Exception {
        fail();
    }

}
