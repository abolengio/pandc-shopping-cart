package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.utils.FileReaderProvider;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.Reader;

//todo should be singleton and fully synchronised
//todo add assumption about hosting on one node only
public class EventSourcingFileShoppingCartReaderWriter implements WritableShoppingCart, ShoppingCartEventSource {

    public static final String SHOPPING_CART_FILE_PATH_PROPERTY = "shopping.cart.file";
    private final String filePath;
    private final FileReaderProvider fileReaderProvider;
    private final ShoppingCartCommandSerializerDeserializer commandSerializerDeserializer;

    public EventSourcingFileShoppingCartReaderWriter(Environment environment,
                                                     FileReaderProvider fileReaderProvider,
                                                     ShoppingCartCommandSerializerDeserializer commandSerializerDeserializer) {
        this.fileReaderProvider = fileReaderProvider;
        this.commandSerializerDeserializer = commandSerializerDeserializer;
        filePath = environment.getProperty(SHOPPING_CART_FILE_PATH_PROPERTY);
        //todo check file access here ?
    }

    @Override
    public void add(String productId, int quantity) {

    }

    @Override
    public void remove(String productId) {
    }

    @Override
    public void updateQuantity(String productId, int quantity) {
    }

    @Override
    public void readInto(WritableShoppingCart writableShoppingCart) {
        Reader fileReader = null;
        try {
            fileReader = fileReaderProvider.getFileReader(filePath);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while( (line = br.readLine()) != null) {
                commandSerializerDeserializer.read(line, writableShoppingCart);
            }
        } catch (FileNotFoundException ignore) {
        } catch (Exception exc) {
            throw new RuntimeException("Error parsing shopping cart file", exc);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                }catch(Exception ignore) {
                }
            }
        }

    }

}
