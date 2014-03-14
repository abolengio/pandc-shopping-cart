package com.ab.cart.integration;

import com.ab.cart.config.spring.ApplicationConfig;
import com.ab.cart.config.spring.IntegrationTestPropertiesConfig;
import com.ab.cart.config.spring.WebMvcConfig;
import com.ab.cart.repository.impl.CsvFileProductProvider;
import com.ab.cart.repository.impl.eventsourced.EventSourcingFileShoppingCartReaderWriter;
import com.ab.cart.rest.controller.UriFor;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.apache.commons.lang.StringUtils.replace;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private Environment environment;

    private void givenProductFileWithContent(String content) throws IOException {
        String productFilePath = environment.getProperty(CsvFileProductProvider.PRODUCT_CSV_FILE_PATH_PROPERTY);
        FileUtils.writeStringToFile(new File(productFilePath), content, Charset.forName("UTF-8"));
    }

    private void givenShoppingCartFileWithContent(String content) throws IOException {
        String shoppingCartFilePath = environment.getProperty(EventSourcingFileShoppingCartReaderWriter.SHOPPING_CART_FILE_PATH_PROPERTY);
        FileUtils.writeStringToFile(new File(shoppingCartFilePath), content, Charset.forName("UTF-8"));
    }

    private String shoppingCartFileContent() throws IOException {
        String shoppingCartFilePath = environment.getProperty(EventSourcingFileShoppingCartReaderWriter.SHOPPING_CART_FILE_PATH_PROPERTY);
        return FileUtils.readFileToString(new File(shoppingCartFilePath), Charset.forName("UTF-8"));     //todo make sure file is actually written in UTF-8
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnCartContent() throws Exception{
        givenProductFileWithContent("1001,test dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00\n" +
                                    "1002,Test Green Shirt,9.90,\n" +
                                    "some-other-id, line without comma at the end, 23");

        givenShoppingCartFileWithContent(   "ADD,1001,2\n" +
                                            "ADD,1002,3\n" +
                                            "UPDATE_QUANTITY,1001,4\n" +
                                            "ADD,some-other-id,1\n" +
                                            "REMOVE,some-other-id");

        mockMvc.perform(get(UriFor.cart))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].productId", is("1001")))
                .andExpect(jsonPath("$.items[0].quantity", is(4)))
                .andExpect(jsonPath("$.items[0].product.name", is("test dress with pink flowers")))
                .andExpect(jsonPath("$.items[1].productId", is("1002")))
                .andExpect(jsonPath("$.items[1].quantity", is(3)))
                .andExpect(jsonPath("$.items[1].product.name", is("Test Green Shirt")))
                .andExpect(jsonPath("$.subTotal.amount", is(149.66)))
                .andExpect(jsonPath("$.subTotal.currency", is("EUR")));
    }

    @Test
    public void shouldAddItem() throws Exception{
        givenProductFileWithContent("1001,test dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00\n" +
                                    "1002,Test Green Shirt,9.90,\n" );

        givenShoppingCartFileWithContent("ADD,1001,1\n");

        mockMvc.perform(post(UriFor.cartItems)
                .contentType(APPLICATION_JSON_UTF8)
                .content("{\"productId\":\"1002\"," +
                        "\"quantity\":2}"))
                .andExpect(status().isOk())
                ;

        assertThat(shoppingCartFileContent(), is("ADD,1001,1\n" +
                "ADD,1002,2\n"));
    }

    @Test
    public void shouldRemoveItem() throws Exception{
        givenProductFileWithContent("1001,test dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00\n" +
                                    "1002,Test Green Shirt,9.90,\n");

        givenShoppingCartFileWithContent("ADD,1001,1\n" +
                "ADD,1002,2\n");

        mockMvc.perform(delete(uriForCartItemWithProductId("1001"))
                )
                .andExpect(status().isOk())
        ;

        assertThat(shoppingCartFileContent(), is(   "ADD,1001,1\n" +
                                                    "ADD,1002,2\n" +
                                                    "REMOVE,1001\n"));
    }

    @Test
    public void shouldUpdateQuantityInItem() throws Exception{
        givenProductFileWithContent("1001,test dress with pink flowers,29.99,2014-02-28:15:00:00-2014-02-28:16:00:00\n" +
                                    "1002,Test Green Shirt,9.90,\n");

        givenShoppingCartFileWithContent("ADD,1001,1\n");

        mockMvc.perform(put(uriForCartItemWithProductId("1002"))
                                        .contentType(APPLICATION_JSON_UTF8)
                                        .content("{" +
                                                    "\"productId\":\"1002\"," +
                                                    "\"quantity\":2" +
                                                 "}"))
                                        .andExpect(status().isOk())
        ;

        assertThat(shoppingCartFileContent(), is(   "ADD,1001,1\n" +
                                                    "UPDATE_QUANTITY,1002,2\n"));
    }

    private String uriForCartItemWithProductId(String productId) {
        return replace(UriFor.cartItem, "{productId}" , productId);
    }
    //todo check with clock set
    //todo deleting or updating item for product which is not in the cart
}
