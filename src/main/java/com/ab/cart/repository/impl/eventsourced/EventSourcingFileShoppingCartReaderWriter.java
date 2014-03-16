package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.utils.FileLineWriter;
import com.ab.cart.utils.FileReaderFactory;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
 * provides synchronised access to file containing content of the shopping basket
 */
public class EventSourcingFileShoppingCartReaderWriter implements WritableShoppingCart, ShoppingCartEventSource {

    public static final String SHOPPING_CART_FILE_PATH_PROPERTY = "shopping.cart.file";
    private final String filePath;
    private final FileReaderFactory fileReaderFactory;
    private final FileLineWriter fileLineWriter;
    private final ShoppingCartCommandSerializerDeserializer commandSerializerDeserializer;

    public EventSourcingFileShoppingCartReaderWriter(Environment environment,
                                                     FileReaderFactory fileReaderFactory,
                                                     FileLineWriter fileLineWriter,
                                                     ShoppingCartCommandSerializerDeserializer commandSerializerDeserializer) {
        this.fileReaderFactory = fileReaderFactory;
        this.fileLineWriter = fileLineWriter;
        this.commandSerializerDeserializer = commandSerializerDeserializer;
        filePath = environment.getProperty(SHOPPING_CART_FILE_PATH_PROPERTY);
    }

    @Override
    public synchronized void add(String productId, int quantity) {
        fileLineWriter.addLine(commandSerializerDeserializer.addCommandFor(productId, quantity));
    }

    @Override
    public synchronized void remove(String productId) {
        fileLineWriter.addLine(commandSerializerDeserializer.removeCommandFor(productId));
    }

    @Override
    public synchronized void updateQuantity(String productId, int quantity) {
        fileLineWriter.addLine(commandSerializerDeserializer.updateQuantityCommandFor(productId, quantity));
    }

    @Override
    public synchronized void readInto(WritableShoppingCart writableShoppingCart) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(fileReaderFactory.getFileReader(filePath));
            String line;
            while( (line = fileReader.readLine()) != null) {
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
