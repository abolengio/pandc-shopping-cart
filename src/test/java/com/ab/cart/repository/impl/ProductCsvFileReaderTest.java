package com.ab.cart.repository.impl;

import com.ab.cart.domain.Product;
import com.ab.cart.utils.FileReaderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductCsvFileReaderTest {

    @Mock
    FileReaderFactory fileReaderFactory;
    @Mock
    ProductCsvEntryParser productCsvEntryParser;

    @Test
    public void shouldParseOneLinerFile() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                                        .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(getReaderWith("one liner"));
        Product product1 = mock(Product.class);
        when(productCsvEntryParser.parseEntry("one liner")).thenReturn(product1);
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                                                                    , fileReaderFactory
                                                                    , productCsvEntryParser);
        List<Product> products = productReader.read();
        assertThat(products, hasSize(1));
        assertThat(products.get(0), is(product1));
    }

    @Test
    public void shouldParseEmptyFile() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                                        .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(getReaderWith(""));
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                                                                    , fileReaderFactory
                                                                    , productCsvEntryParser);
        List<Product> products = productReader.read();
        assertThat(products, hasSize(0));
    }

    @Test
    public void shouldParseFileContainingSeveralLines() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                                        .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(getReaderWith("first line\nsecond line"));
        Product product1 = mock(Product.class);
        when(productCsvEntryParser.parseEntry("first line")).thenReturn(product1);
        Product product2 = mock(Product.class);
        when(productCsvEntryParser.parseEntry("second line")).thenReturn(product2);
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                                                                    , fileReaderFactory
                                                                    , productCsvEntryParser);
        List<Product> products = productReader.read();
        assertThat(products, hasSize(2));
        assertThat(products.get(0), is(product1));
        assertThat(products.get(1), is(product2));
    }

    @Test
    public void shouldParseFileContainingEmptyLines() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                                        .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        String content = "first line\n\nsecond line\n\n  ";
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(getReaderWith(content));
        Product product1 = mock(Product.class);
        when(productCsvEntryParser.parseEntry("first line")).thenReturn(product1);
        Product product2 = mock(Product.class);
        when(productCsvEntryParser.parseEntry("second line")).thenReturn(product2);
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                                                                    , fileReaderFactory
                                                                    , productCsvEntryParser);
        List<Product> products = productReader.read();
        assertThat(products, hasSize(2));
        assertThat(products.get(0), is(product1));
        assertThat(products.get(1), is(product2));
    }

    @Test
    public void shouldCloseReaderAtTheEnd() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        BufferedReader fileReader = getReaderWith("");
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(fileReader);
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                , fileReaderFactory
                , productCsvEntryParser);
        productReader.read();
        assertThat(readerIsClosed(fileReader), is(true));
    }

    @Test
    public void shouldCloseReaderWhenExceptionOccursWhileParsing() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                .withProperty(ProductCsvFileReader.PRODUCT_CSV_FILE_PATH_PROPERTY, "blah-file");
        BufferedReader fileReader = getReaderWith("first line\n");
        when(fileReaderFactory.getFileReader("blah-file")).thenReturn(fileReader);
        when(productCsvEntryParser.parseEntry(anyString())).thenThrow(new RuntimeException("some error"));
        ProductCsvFileReader productReader = new ProductCsvFileReader(environment
                , fileReaderFactory
                , productCsvEntryParser);
        try {
            productReader.read();
            fail("Exception expected");
        }catch(Exception exc) {
            assertThat(readerIsClosed(fileReader), is(true));
        }
    }

    private boolean readerIsClosed(Reader reader) {
        try {
            reader.read();
        } catch (IOException exc) {
            return exc.getMessage().contains("Stream closed");
        }
        return false;
    }

    private BufferedReader getReaderWith(String content) {
        return new BufferedReader(new StringReader(content));
    }
}
