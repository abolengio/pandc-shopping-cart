package com.ab.cart.integration;

import com.ab.cart.config.spring.ApplicationConfig;
import com.ab.cart.config.spring.IntegrationTestPropertiesConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.rest.controller.UriFor;
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

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, ApplicationConfig.class, IntegrationTestPropertiesConfig.class})
@WebAppConfiguration
public class ShoppingCartIntegrationTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnCartContent() throws Exception{

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].productId", is("1001")))
                .andExpect(jsonPath("$.items[0].quantity", is(4)))
                .andExpect(jsonPath("$.items[1].productId", is("1002")))
                .andExpect(jsonPath("$.items[1].quantity", is(3)))
                .andExpect(jsonPath("$.subTotal", is(149.66)));
    }

    //todo check with clock set
}
