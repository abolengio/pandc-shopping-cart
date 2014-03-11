package com.ab.cart.rest.controller;

import com.ab.cart.domain.ReadableShoppingCart;
import com.ab.cart.domain.ReadableShoppingCartProvider;
import com.ab.cart.rest.resource.ShoppingCartResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShoppingCartController {

    private ReadableShoppingCartProvider readableShoppingCartProvider;

    @Autowired
    public ShoppingCartController( ReadableShoppingCartProvider readableShoppingCartProvider) {
        this.readableShoppingCartProvider = readableShoppingCartProvider;
    }

    @RequestMapping(value = UriFor.cart, method = RequestMethod.GET)
    @ResponseBody
    public ShoppingCartResource shoppingCart() {
        ReadableShoppingCart readableShoppingCart = readableShoppingCartProvider.getReadableShoppingCart();
        return new ShoppingCartResource(readableShoppingCart); //todo use converter rather than constructor

    }

}
