package com.ab.cart.rest.controller;

import com.ab.cart.domain.ReadableShoppingCart;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.domain.WritableShoppingCart;
import com.ab.cart.rest.resource.ShoppingCartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShoppingCartController {

    private ReadableShoppingCartProvider readableShoppingCartProvider;
    private WritableShoppingCart writableShoppingCart;

    @Autowired
    public ShoppingCartController( ReadableShoppingCartProvider readableShoppingCartProvider, WritableShoppingCart writableShoppingCart) {
        this.readableShoppingCartProvider = readableShoppingCartProvider;
        this.writableShoppingCart = writableShoppingCart;
    }

    @RequestMapping(value = UriFor.cart, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartResource shoppingCart() {
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart); //todo use converter rather than constructor

    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        ex.printStackTrace();
    }

    @RequestMapping(value = UriFor.cartItems, method = RequestMethod.POST)
    @ResponseBody
    public ShoppingCartResource addItem(@RequestBody CartItemParameter cartItem) {
        System.out.println("HURRAY - " + writableShoppingCart.getClass().getName());
        System.out.println("CartItemParameter - " + cartItem.getProductId());
        writableShoppingCart.add(cartItem.getProductId(), cartItem.getQuantity());
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart); //todo refactor to avoid duplication
    }

}
