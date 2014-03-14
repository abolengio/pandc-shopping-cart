package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.utils.FileLineWriter;
import com.ab.cart.utils.FileReaderProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.env.MockEnvironment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EventSourcingFileShoppingCartReaderWriterTest {

    public static final String DEFAULT_SHOPPING_CART_FILE_PATH = "shopping-cart-file-path";
    @Mock
    FileReaderProvider fileReaderProvider;
    @Mock
    WritableShoppingCart writableShoppingCart;
    @Mock
    ShoppingCartCommandSerializerDeserializer commandSerializerDeserializer;
    @Mock
    FileLineWriter fileLineWriter;


    MockEnvironment environment;

    @Before
    public void setup() {
        environment = new MockEnvironment()
                .withProperty(EventSourcingFileShoppingCartReaderWriter.SHOPPING_CART_FILE_PATH_PROPERTY, DEFAULT_SHOPPING_CART_FILE_PATH);
    }

    public void givenShoppingCartFileWithContent(String content) throws FileNotFoundException {
        when(fileReaderProvider.getFileReader(DEFAULT_SHOPPING_CART_FILE_PATH)).thenReturn(new StringReader(content));
    }

    @Test
    public void shouldReadDataFromFileSpecifiedByProperty() throws IOException {
        MockEnvironment environment = new MockEnvironment()
                .withProperty(EventSourcingFileShoppingCartReaderWriter.SHOPPING_CART_FILE_PATH_PROPERTY, "specified file path");
        String content = "ADD,product-id-1,1";
        when(fileReaderProvider.getFileReader(anyString())).thenReturn(new StringReader(content));
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        reader.readInto(writableShoppingCart);
        verify(fileReaderProvider).getFileReader("specified file path");

    }

    @Test
    public void shouldCloseReaderAfterReading() throws IOException {
        StringReader stringReader = new StringReader("dont really care");
        when(fileReaderProvider.getFileReader(anyString())).thenReturn(stringReader);
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        verify(fileReaderProvider, never()).getFileReader(anyString());
        reader.readInto(writableShoppingCart);
        assertThat(readerIsClosed(stringReader), is(true));
    }

    @Test
    public void shouldCloseReaderWhenExceptionOccurs() throws IOException {
        StringReader stringReader = new StringReader("don't really care");
        when(fileReaderProvider.getFileReader(anyString())).thenReturn(stringReader);
        doThrow(new RuntimeException()).when(commandSerializerDeserializer).read("don't really care", writableShoppingCart);
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        try {
            reader.readInto(writableShoppingCart);
            fail("Exception expected");
        } catch (RuntimeException exc) {
            assertThat(readerIsClosed(stringReader), is(true));
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

    @Test
    public void shouldReadFileWithOneLine() throws IOException {
        givenShoppingCartFileWithContent("one line content");
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        reader.readInto(writableShoppingCart);
        verify(commandSerializerDeserializer).read("one line content",writableShoppingCart);
    }

    //todo test the order ? - actually dont and remove the order from other places, but add time when command was recorded

    @Test
    public void shouldReadFileWithMultipleLines() throws IOException {
        givenShoppingCartFileWithContent("first line content\nsecond line content\nthird line content");
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        reader.readInto(writableShoppingCart);
        verify(commandSerializerDeserializer).read("first line content",writableShoppingCart);
        verify(commandSerializerDeserializer).read("second line content",writableShoppingCart);
        verify(commandSerializerDeserializer).read("third line content",writableShoppingCart);
    }

    @Test
    public void shouldNotTouchShoppingCartIfFileIsEmpty() throws IOException {
        givenShoppingCartFileWithContent("");
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        reader.readInto(writableShoppingCart);
        verifyZeroInteractions(commandSerializerDeserializer);
    }

    @Test
    public void shouldNotTouchShoppingCartIfFileDoesNotExist() throws IOException {
        when(fileReaderProvider.getFileReader(anyString())).thenThrow(new FileNotFoundException());
        EventSourcingFileShoppingCartReaderWriter reader = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        reader.readInto(writableShoppingCart);
        verifyZeroInteractions(commandSerializerDeserializer);
    }

    @Test
    public void shouldWriteAddCommand() {
        when(commandSerializerDeserializer.addCommandFor("some-id",78)).thenReturn("serialized command");
        EventSourcingFileShoppingCartReaderWriter writer = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        writer.add("some-id", 78);
        verify(fileLineWriter).addLine("serialized command");

    }

    @Test
    public void shouldWriteRemoveCommand() {
        when(commandSerializerDeserializer.removeCommandFor("some-id")).thenReturn("serialized command");
        EventSourcingFileShoppingCartReaderWriter writer = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        writer.remove("some-id");
        verify(fileLineWriter).addLine("serialized command");
    }

    @Test
    public void shouldWriteUpdateCommand() {
        when(commandSerializerDeserializer.updateQuantityCommandFor("some-id", 18)).thenReturn("serialized command");
        EventSourcingFileShoppingCartReaderWriter writer = new EventSourcingFileShoppingCartReaderWriter(environment, fileReaderProvider,fileLineWriter,commandSerializerDeserializer);
        writer.updateQuantity("some-id", 18);
        verify(fileLineWriter).addLine("serialized command");
    }

}
