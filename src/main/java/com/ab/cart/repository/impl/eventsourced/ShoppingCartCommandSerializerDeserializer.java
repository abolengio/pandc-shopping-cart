package com.ab.cart.repository.impl.eventsourced;

import com.ab.cart.domain.WritableShoppingCart;

import static java.lang.String.format;

public class ShoppingCartCommandSerializerDeserializer {

    public static final String ADD_COMMAND = "ADD";
    public static final String REMOVE_COMMAND = "REMOVE";
    public static final String UPDATE_COMMAND = "UPDATE_QUANTITY";

    public ShoppingCartCommandSerializerDeserializer() {
    }

    public void read(String line, WritableShoppingCart writableShoppingCart) {
        String[] commandData = line.split(",");
        String command = commandData[0];
        String productId = commandData[1];
        if(ADD_COMMAND.equalsIgnoreCase(command)) {
            int quantity = Integer.parseInt(commandData[2]);
            writableShoppingCart.add(productId, quantity);
        } else if(UPDATE_COMMAND.equalsIgnoreCase(command)) {
            int quantity = Integer.parseInt(commandData[2]);
            writableShoppingCart.updateQuantity(productId, quantity);
        } else if(REMOVE_COMMAND.equalsIgnoreCase(command)) {
            writableShoppingCart.remove(productId);
        } else {
            throw new IllegalArgumentException("unexpected shopping cart command: " + command);
        }
    }

    public String addCommandFor(String productId, int quantity) {
        return format("ADD,%s,%s",productId,quantity);
    }

    public String removeCommandFor(String productId) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public String updateQuantityCommandFor(String productId, int quantity) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
